package com.redissession.repository;

import java.util.Map;

import com.redissession.model.RedisSampleObject;

public interface RedisRepository {

    /**
     * Return all RedisSampleObject
     * @param keyName
     */
    Map<Object, Object> getAllRedisSampleObject(String keyName);

    /**
     * Add key-value pair to Redis.
     */
    void add(String s, RedisSampleObject movie);

    /**
     * Delete a key-value pair in Redis.
     */
    void delete(String keyName, String value);
    
    /**
     * find a RedisSampleObject
     */
    RedisSampleObject getRedisSampleObject(String id);

    public String getKEY();
    
}
