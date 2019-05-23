package com.my.test.demo.impl;

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
    RedissonClient redissonClient;

    @Override
    public Boolean getReentrantLock(String lockName) {
        RLock lock = redissonClient.getLock(lockName);
        try{
            // 1. 最常见的使用方法
            //lock.lock();
            // 2. 支持过期解锁功能,10秒钟以后自动解锁, 无需调用unlock方法手动解锁
            //lock.lock(10, TimeUnit.SECONDS);
            // 3. 尝试加锁，最多等待3秒，上锁以后10秒自动解锁
            Boolean res = lock.tryLock(3, 10, TimeUnit.SECONDS);
            return res;
        } catch (InterruptedException e) {
            log.error(e.getMessage(),e);
            return false;
        } finally {
            lock.unlock();
        }
    }
}
