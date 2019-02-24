package com.my.test.demo.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisUtil {

    // Redis服务器IP
    private static String ADDRESS = "47.98.238.150";

    // Redis的端口号
    private static int PORT = 6397;

    // 访问密码
    private static String PASSWORD = "shi974075295";

    private static JedisPool jedisPool = null;

    /**
     * 初始化Redis连接池
     */
    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxIdle(10);
            config.setMaxTotal(1000);
            config.setMaxWaitMillis(10*1000);
            config.setTestOnBorrow(true);// borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
            config.setTestOnReturn(true);// return一个jedis实例给pool时，是否检查连接可用,设置为true时，返回的对象如果验证失败，将会被销毁，否则返回
            jedisPool = new JedisPool(config,ADDRESS,PORT,3*1000,PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Jedis实例
     * @return
     */
    public synchronized static Jedis getJedis() {
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
