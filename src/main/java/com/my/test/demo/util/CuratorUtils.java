package com.my.test.demo.util;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * zk客户端工具
 * Created by weibin on 2017/12/14.
 */
public class CuratorUtils {

    /**
     * 失败重试间隔时间
     */
    private static final int baseSleepTimeMs = 1000;

    /**
     * 失败重试次数
     */
    private static final int maxRetries = 3;

    /**
     * zk集群地址
     */
//    private static String ConnectString = "47.96.160.128:2181";

    /**
     * zk连接实例
     */
    private volatile static CuratorFramework curatorFramework = null;

    /**
     * 会话超时时间
     */
    private static int sessionTimeOut = 1000*6;

    /**
     * 连接超时时间
     */
    private static int connectionTimeout = 1000*6;


    /**
     * 全局排他锁
     */
    private volatile static InterProcessSemaphoreMutex xLocks;

    /**
     * 全局可重入锁
     */
    private volatile static InterProcessMutex rLock;

    /**
     * 全局排他锁节点路径
     */
    private static final String X_LOCK_PATH = "/xlock/Global";

    /**
     * 全局可重入锁节点路径
     */
    private static final String R_LOCK_PATH = "/rlock/Global";

    /**
     * 私有构造，拒绝外部直接New
     */
    private CuratorUtils(String ConnectString) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(baseSleepTimeMs, maxRetries);
        curatorFramework = CuratorFrameworkFactory.builder().connectString(ConnectString).retryPolicy(retryPolicy)
                .sessionTimeoutMs(sessionTimeOut).connectionTimeoutMs(connectionTimeout).build();
        CuratorUtils.start();
        xLocks = new InterProcessSemaphoreMutex(curatorFramework,X_LOCK_PATH);
        rLock = new InterProcessMutex(curatorFramework,R_LOCK_PATH);
    }

    /**
     * 双重锁检查
     * @return
     */
    public static CuratorFramework getInstance(String ConnectString) {
        if (curatorFramework == null) {
            synchronized(CuratorUtils.class){
                if (curatorFramework == null) {
                    new CuratorUtils(ConnectString);
                }
            }
        }
        return curatorFramework;
    }



    public static void start() {
        curatorFramework.start();
    }


    public static void close() {
        curatorFramework.close();
    }

    /**
     * 根据路径获取znodes节点信息
     * @param parentPath
     * @return
     */
    public static List<String> getZnodes(String parentPath, String ConnectString){
        try{
            CuratorFramework curatorFramework =  CuratorUtils.getInstance(ConnectString);
            return curatorFramework.getChildren().forPath(parentPath);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        } finally {
            //TODO
            CuratorUtils.close();
        }
    }

    /**
     * 获取排他锁
     * 尝试10秒获取锁资源
     * @param lockPath
     * @return
     */
    public static Boolean getxLock(String ConnectString){
        try {
            CuratorFramework curatorFramework =  CuratorUtils.getInstance(ConnectString);
            return xLocks.acquire(10, TimeUnit.SECONDS);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 释放排他锁
     */
    public static void xLockRelease(){
        try {
            xLocks.release();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args){


//        System.out.println( "获取锁："+getxLock());
//
//        System.out.println( "获取锁："+ getxLock());
//
//        xLockRelease();
//
//        System.out.println( "获取锁："+ getxLock());
//
//        xLockRelease();
//        System.out.println("释放锁");
    }

}
