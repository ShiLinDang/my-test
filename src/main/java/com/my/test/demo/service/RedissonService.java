package com.my.test.demo.service;

import org.redisson.api.RLock;

/**
 * @Description:
 * @author:dangshilin
 * @date 2019/5/2310:48
 */
public interface RedissonService {

    /**
     * 获取锁
     * @param lockKey
     * @return
     */
    RLock getLock(String lockKey);

    /**
     * 获取锁,指定到期时间expireTime以后自动解锁
     * @param lockKey
     * @param expireTime
     */
    RLock getLock(String lockKey,Long expireTime);

    /**
     * 尝试获取锁,最多等待waitTime时间,expireTime以后自动解锁
     * @param lockKey
     * @param waitTime
     * @param expireTime
     * @return 加锁是否成功
     */
    Boolean tryLock(String lockKey,Long waitTime,Long expireTime);

    /**
     * 获取公平锁
     * @param lockKey
     * @return
     */
    RLock getFairLock(String lockKey);

    /**
     * 尝试获取公平锁,最多等待waitTime时间,expireTime以后自动解锁
     * @param lockKey
     * @param waitTime
     * @param expireTime
     * @return
     */
    Boolean getFairLock(String lockKey,Long waitTime,Long expireTime);
}
