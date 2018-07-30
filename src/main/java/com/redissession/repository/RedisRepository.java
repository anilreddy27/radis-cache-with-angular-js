package com.redissession.repository;

import java.util.Map;

import com.redissession.model.RedisSampleObject;

public interface RedisRepository {

    /**
     * Return all RedisSampleObject
     */
    Map<Object, Object> getAllRedisSampleObject();

    /**
     * Add key-value pair to Redis.
     */
    void add(RedisSampleObject movie);

    /**
     * Delete a key-value pair in Redis.
     */
    void delete(String id);
    
    /**
     * find a RedisSampleObject
     */
    RedisSampleObject getRedisSampleObject(String id);

    public String getKEY();
    
}
