package com.my.test.demo.impl;

import com.my.test.demo.service.RedisUtilService;
import com.my.test.demo.util.JedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * @Description:
 * @author:dangshilin
 * @date 2019/2/2215:24
 */
@Service
@Slf4j
public class RedisUtilServiceImpl implements RedisUtilService {

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX"; //key不存在时，我们进行set操作；若key已经存在，则不做任何操作；
    private static final String SET_WITH_EXPIRE_TIME = "PX"; //要给这个key加一个过期的设置，具体时间由第五个参数决定。
    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * 加锁
     *
     * @param lockName 锁名，对应被争用的共享资源
     * @param randomValue 随机值，需要保持全局唯一，便于释放时校验锁的持有者
     * @param expireTime 过期时间，到期后自动释放，防止出现问题时死锁，资源无法释放
     * @return
     */
    @Override
    public Boolean acquireLock(String lockName, String randomValue, int expireTime) {
        Jedis jedis = JedisUtil.getJedis();
        try {
            while (true){
                String result = jedis
                        .set(lockName, randomValue, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
                if(LOCK_SUCCESS.equals(result)){
                    log.info("【Redis lock】success to acquire lock for [ "+lockName+" ],expire time:"+expireTime+"ms");
                    return true;
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            if(null != jedis){
                jedis.close();
            }
        }
        log.info("【Redis lock】failed to acquire lock for [ "+lockName+" ]");
        return false;
    }

    /**
     * redis释放锁
     * watch和muti命令保证释放时的对等性，防止误解锁
     *
     * @param lockName 锁名，对应被争用的共享资源
     * @param randomValue 随机值，需要保持全局唯一，以检验锁的持有者
     * @return 是否释放成功
     */
    @Override
    public Boolean releaseLock(String lockName, String randomValue) {
        Jedis jedis = JedisUtil.getJedis();
        try{
            jedis.watch(lockName);//watch监控
            if(randomValue.equals(jedis.get(lockName))){
                Transaction multi = jedis.multi();//开启事务
                multi.del(lockName);//添加操作到事务
                List<Object> exec = multi.exec();//执行事务
                if(RELEASE_SUCCESS.equals(exec.size())){
                    log.info("【Redis lock】success to release lock for [ "+lockName+" ]");
                    return true;
                }
            }
        }catch (Exception ex){
            log.info("【Redis lock】failed to release lock for [ "+lockName+" ]");
            ex.printStackTrace();
        }finally {
            if(null != jedis){
                jedis.unwatch();
                jedis.close();
            }
        }
        return false;
    }
}
