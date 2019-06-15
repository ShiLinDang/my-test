package com.my.test.demo.impl;

import com.my.test.demo.config.RedissonManager;
import com.my.test.demo.service.RedissonService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @author:dangshilin
 * @date 2019/5/2310:48
 */
@Service
@Slf4j
public class RedissonServiceImpl implements RedissonService {

    @Autowired
    private RedissonManager redissonManager;

    /**
     * 获取锁
     * @param lockKey
     * @return
     */
    @Override
    public RLock getLock(String lockKey) {
        RLock lock = redissonManager.getRedisson().getLock(lockKey);
        lock.lock();
        return lock;
    }

    /**
     * 获取锁,指定到期时间expireTime以后自动解锁, 无需调用unlock方法手动解锁
     * @param lockKey
     * @param expireTime
     */
    @Override
    public RLock getLock(String lockKey,Long expireTime) {
        RLock lock = redissonManager.getRedisson().getLock(lockKey);
        lock.lock(expireTime, TimeUnit.SECONDS);
        return lock;
    }

    /**
     * 尝试获取锁,最多等待waitTime时间,expireTime以后自动解锁
     *
     * @param lockKey
     * @param waitTime
     * @param expireTime
     * @return 加锁是否成功
     */
    @Override
    public Boolean tryLock(String lockKey, Long waitTime, Long expireTime) {
        RLock lock = redissonManager.getRedisson().getLock(lockKey);
        try {
            // 尝试加锁，最多等待3秒，上锁以后10秒自动解锁
            boolean res = lock.tryLock(waitTime, expireTime, TimeUnit.SECONDS);
            return res;
        } catch (InterruptedException e) {
            log.error(e.getMessage(),e);
            return false;
        }finally {
            lock.unlock();
        }
    }

    /**
     * 获取公平锁
     *
     * @param lockKey
     * @return
     */
    @Override
    public RLock getFairLock(String lockKey) {
        RLock fairLock = redissonManager.getRedisson().getFairLock(lockKey);
        fairLock.lock();
        return fairLock;
    }

    /**
     * 尝试获取公平锁,最多等待waitTime时间,expireTime以后自动解锁
     *
     * @param lockKey
     * @param waitTime
     * @param expireTime
     * @return
     */
    @Override
    public Boolean getFairLock(String lockKey, Long waitTime, Long expireTime) {
        RLock fairLock = redissonManager.getRedisson().getFairLock(lockKey);
        try {
            // 尝试加锁，最多等待3秒，上锁以后10秒自动解锁
            boolean res = fairLock.tryLock(waitTime, expireTime, TimeUnit.SECONDS);
            return res;
        } catch (InterruptedException e) {
            log.error(e.getMessage(),e);
            return false;
        }finally {
            fairLock.unlock();
        }
    }
}
