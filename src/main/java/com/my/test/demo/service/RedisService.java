package com.my.test.demo.service;

import org.springframework.data.redis.core.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface RedisService {

    /**获得客户端列表 */
    List<?> getClients();

    /**
     * K-V 存入缓存
     * @param k
     * @param t
     * @param <T>
     */
    <T> void setValue(String k, T t);

    <T> void setValue(String k, T t, Long l);

    <T> void setValue(String k, T t, Long l, TimeUnit timeUnit);

    /**
     * K-V根据K取值
     * @param k
     * @param <T>
     * @return
     */
    <T> T getValue(String k);

    /**
     * 根据K值删除缓存
     * @param k
     */
    void delValue(String k);

    <E> List<E> getListAll(String k);

    <E> List<E> getListByPage(String k, Long startNum, Long endNum);

    <E> Long rightPushList(String k, E e);

    <E> Long rightPushAllList(String k, E... e);

    <E extends Collection> Long rightPushAllList(String k, E e);

    <E> Long leftPushList(String k, E e);

    <E> Long leftPushAllList(String k, E... e);

    <E extends Collection> Long leftPushAllList(String k, E e);

    void delListByKey(String k);

    <T> void putMap(String h, String hk, T t);

    <T extends Map> void  putMap(String h, T t);

    <T> T getMapValueByKey(String h, String hk);

    <T extends Map> T getMapByKey(String h);

    void delMapByKey(String h);

    <T> void delMap(String h, T... t);

    RedisTemplate getRedisTemplate();

    ValueOperations getValueOperations();

    HashOperations getHashOperations();

    SetOperations getSetOperations();

    ListOperations getListOperations();

    ZSetOperations getzSetOperations();

    /**
     * value自增
     * @param key
     * @param liveTime 有效时间（首次设置有效）
     * @param timeUnit 时间单位
     * @return
     */
    Long increment(String key, long liveTime, TimeUnit timeUnit);

}
