package com.my.test.demo.service;

/**
 * @Description:
 * @author:dangshilin
 * @date 2019/5/2310:48
 */
public interface RedissonService {
    Boolean getReentrantLock(String lockName);
}
