package com.deepthink.common.cache;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @program: vending_admin
 * @description: 本地缓存
 * @author: td
 * @create: 2020-03-25 15:35
 **/
@Component
public class LocalCacher implements InitializingBean {
    private HashMap<String, Object> cache;

    private final TimeUnit defaultTimeUnit = TimeUnit.SECONDS;

    private  HashMap<String, Date> expireTimes;

    public synchronized void put(String key, Object value) {
        put(key, value, 0);
    }

    public synchronized boolean contain(String key) {
        return cache.containsKey(key);
    }

    public synchronized void remove(String key) {
        cache.remove(key);
        expireTimes.remove(key);
    }
    /** 
    * @Description: 转换的类型是由调用者确定的，如果类型错误将导致失败 
    * @Param:  
    * @return:  
    * @Author: td 
    * @Date: 2020/3/25 
    */ 
    public synchronized <T> T get(String key) {
        Date date = expireTimes.get(key);
        if (date == null) {
            return (T) cache.get(key);
        }
        if (isExpire(date)) {
            cache.remove(key);
            expireTimes.remove(key);
            return null;
        }
        return (T) cache.get(key);
    }


    private  boolean isExpire(Date date) {
        return new Date().after(date);
    }

    public synchronized void put(String key, Object value, TimeUnit timeUnit, long expireTime) {
        assert expireTime >= 0;
        cache.put(key, value);
        if (expireTime > 0) {
            Date now = new Date();
            now.setTime(System.currentTimeMillis() + timeUnit.toMillis(expireTime));
            expireTimes.put(key, now);
        }
    }

    public synchronized void put(String key, Object value, long expireTime) {
        put(key, value, defaultTimeUnit, expireTime);
    }

    public synchronized void clear() {
        cache.clear();
        expireTimes.clear();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        cache = new HashMap<>(16);
        expireTimes = new HashMap<>(16);
    }
}
