package com.my.test.demo.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;

import java.util.concurrent.TimeUnit;

/**
 * 排他锁
 * Created by weibin on 2017/12/15.
 */
public class ExclusiveLock {

    private InterProcessSemaphoreMutex interProcessSemaphoreMutex;

    public ExclusiveLock(CuratorFramework curatorFramework, String lockPath){
        this.interProcessSemaphoreMutex = new InterProcessSemaphoreMutex(curatorFramework,lockPath);
    }

    /**
     * 获取锁
     * @param time time时间内未获得锁，则抛出异常
     * @param timeUnit 时间单位
     * @return
     */
    public Boolean getLock(long time, TimeUnit timeUnit){
       try{
           return this.interProcessSemaphoreMutex.acquire(time,timeUnit);
       }catch (Exception e){
           return false;
        }
    }

    /**
     * 获取锁
     * 此方法如未得到锁，则会阻塞，直至获取到锁
     */
    public void getLock(){
        try{
            this.interProcessSemaphoreMutex.acquire();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 释放锁
     * 每一次调用lock()获取锁，就需要调用lockRelease()方法来平衡
     */
    public void lockRelease(){
        try {
            this.interProcessSemaphoreMutex.release();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
