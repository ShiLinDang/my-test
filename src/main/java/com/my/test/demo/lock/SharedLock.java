package com.my.test.demo.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import java.util.concurrent.TimeUnit;

/**
 * 共享锁
 * Created by weibin on 2017/12/15.
 */
public class SharedLock {

    private InterProcessMutex interProcessMutex;

    public SharedLock(CuratorFramework curatorFramework, String lockPath){
        this.interProcessMutex = new InterProcessMutex(curatorFramework,lockPath);
    }

    /**
     * 获取锁
     */
    public void getLock(){
        try{
            this.interProcessMutex.acquire();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     *获取锁
     * @param time
     * @param timeUnit
     */
    public void getLock(long time, TimeUnit timeUnit){
        try{
            this.interProcessMutex.acquire(time,timeUnit);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 解锁
     * 加锁和解锁次数要平衡
     */
    public void lockRelease(){
        try{
            this.interProcessMutex.release();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
