package com.my.test.demo.impl;

import com.my.test.demo.service.RedisUtilService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * @Description:
 * @author:dangshilin
 * @date 2019/2/2215:24
 */
@Service
public class RedisUtilServiceImpl implements RedisUtilService {

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX"; //key不存在时，我们进行set操作；若key已经存在，则不做任何操作；
    private static final String SET_WITH_EXPIRE_TIME = "PX"; //要给这个key加一个过期的设置，具体时间由第五个参数决定。
    private static final Long RELEASE_SUCCESS = 1L;

    private String host = "47.98.238.150";
    private Integer port = 6397;
    private String password = "shi974075295";

}
