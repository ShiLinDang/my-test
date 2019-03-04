package com.my.test.demo.impl;

import com.my.test.demo.service.RedisService;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Descriptions: Redis操作集合封装，基于Redis为单线程，不考虑并发线程安全问题
 */
@Service("redisService")
public class RedisServiceImpl implements RedisService {

    /* redisTemplate模版 */
    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;
//    /* StringRedisTemplate模版 */
//    @Resource(name = "redisTemplate")
//    private StringRedisTemplate stringRedisTemplate;
    /* K-V操作模版 */
    @Resource(name = "redisTemplate")
    private ValueOperations valueOperations;
    /* Map操作模版 */
    @Resource(name = "redisTemplate")
    private HashOperations hashOperations;
    /* ZSet操作模版 */
    @Resource(name = "redisTemplate")
    private ZSetOperations zSetOperations;
    /* List操作模版 */
    @Resource(name = "redisTemplate")
    private ListOperations listOperations;
    /* Set操作模版 */
    @Resource(name = "redisTemplate")
    private SetOperations setOperations;

    /**获得客户端列表 */
    public List<?> getClients(){
        return redisTemplate.getClientList();
    }

    /**
     * K-V 存入缓存
     * @param k
     * @param t
     * @param <T>
     */
    public <T> void setValue(String k,T t){
        this.valueOperations.set(k,t);
    }

    public <T> void setValue(String k, T t,Long l){
        this.valueOperations.set(k,t,l);
    }

    public <T> void setValue(String k, T t, Long l, TimeUnit timeUnit){
        this.valueOperations.set(k,t,l,timeUnit);
    }

    /**
     * K-V根据K取值
     * @param k
     * @param <T>
     * @return
     */
    public <T> T getValue(String k){
        return (T) this.valueOperations.get(k);
    }

    /**
     * 根据K值删除缓存
     * @param k
     */
    public void delValue(String k){
        this.valueOperations.getOperations().delete(k);
    }

    public <E> List<E> getListAll(String k){
        return this.listOperations.range(k,0,-1);
    }

    public <E> List<E> getListByPage(String k,Long startNum,Long endNum){
        return this.listOperations.range(k,startNum,endNum);
    }

    public <E> Long rightPushList(String k,E e){
       return this.listOperations.rightPush(k,e);
    }

    public <E> Long rightPushAllList(String k,E... e){
        return this.listOperations.rightPush(k,e);
    }

    public <E extends Collection> Long rightPushAllList(String k, E e){
        return this.listOperations.rightPushAll(k,e);
    }

    public <E> Long leftPushList(String k,E e){
        return this.listOperations.leftPush(k,e);
    }

    public <E> Long leftPushAllList(String k,E... e){
        return this.listOperations.leftPushAll(k,e);
    }

    public <E extends Collection> Long leftPushAllList(String k,E e){
        return this.listOperations.leftPushAll(k,e);
    }

    public void delListByKey(String k){
        this.listOperations.getOperations().delete(k);
    }

    public <T> void putMap(String h,String hk,T t){
        this.hashOperations.put(h,hk,t);
    }

    public <T extends Map> void  putMap(String h,T t){
        this.hashOperations.putAll(h,t);
    }

    public <T> T getMapValueByKey(String h,String hk){
        return (T) this.hashOperations.get(h,hk);
    }

    public <T extends Map> T getMapByKey(String h){
        return (T) this.hashOperations.entries(h);
    }

    public void delMapByKey(String h){
        this.hashOperations.getOperations().delete(h);
    }

    public <T> void delMap(String h,T... t){
        this.hashOperations.delete(h,t);
    }

    public RedisTemplate getRedisTemplate(){
        return this.redisTemplate;
    }

    public ValueOperations getValueOperations() {
        return this.valueOperations;
    }

    public HashOperations getHashOperations() { return this.hashOperations; }

    public SetOperations getSetOperations() {
        return this.setOperations;
    }

    public ListOperations getListOperations() {
        return this.listOperations;
    }

    public ZSetOperations getzSetOperations() {
        return this.zSetOperations;
    }

    @Override
    public Long increment(String key, long liveTime, TimeUnit timeUnit) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        long increment = entityIdCounter.getAndIncrement();
        if (increment == 0 && liveTime > 0) {//初始设置过期时间
            entityIdCounter.expire(liveTime, timeUnit);
        }
        return increment;
    }

}
