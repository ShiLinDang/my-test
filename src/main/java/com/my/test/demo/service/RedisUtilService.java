package com.my.test.demo.service;

/**
 * @Description:
 * @author:dangshilin
 * @date 2019/2/2215:23
 */
public interface RedisUtilService {
    Boolean acquireLock(String lockName,String randomValue,int expireTime);// 加锁
    Boolean releaseLock(String lockName,String randomValue);// 解锁
}
