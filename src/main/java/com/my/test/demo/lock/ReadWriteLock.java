package com.my.test.demo.lock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;

import java.util.concurrent.TimeUnit;

/**
 * 读写锁
 * Created by weibin on 2017/12/15.
 */
public class ReadWriteLock {

    private InterProcessReadWriteLock interProcessReadWriteLock;

    public ReadWriteLock(CuratorFramework curatorFramework, String lockPath){
        this.interProcessReadWriteLock = new InterProcessReadWriteLock(curatorFramework,lockPath);
    }

    /**
     * 获取读锁对象
     * @return
     */
    public InterProcessMutex getReadLockObject(){
        return this.interProcessReadWriteLock.readLock();
    }

    /**
     * 获取写锁对象
     * @return
     */
    public InterProcessMutex getWriteLockObject(){
        return this.interProcessReadWriteLock.writeLock();
    }

    /**
     * 读锁释放
     */
    public void readLockRelease(){
        try {
            this.interProcessReadWriteLock.readLock().release();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 写锁释放
     */
    public void writeLockRelease(){
        try {
            this.interProcessReadWriteLock.writeLock().release();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取读锁，未获得锁将阻塞，知道获得锁
     * @throws Exception
     */
    public void readLock() throws Exception {
        this.getReadLockObject().acquire();
    }

    /**
     * 获取读锁，在规定之间内未获得所则直接抛出异常，获取锁失败，不会阻塞
     * @param time
     * @param timeUnit
     * @return
     */
    public Boolean readLock(long time, TimeUnit timeUnit){
        try {
            return this.getReadLockObject().acquire(time,timeUnit);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取写锁，如未得到写锁，则阻塞。直到获得写锁
     * @throws Exception
     */
    public void writeLock() throws Exception {
        this.getWriteLockObject().acquire();
    }

    /**
     * 获取写锁，如在规定时间内未获得锁，则抛出异常，不会阻塞
     * @param time
     * @param timeUnit
     * @return
     */
    public Boolean writeLock(long time,TimeUnit timeUnit){
        try {
            return this.getWriteLockObject().acquire(time,timeUnit);
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

}
