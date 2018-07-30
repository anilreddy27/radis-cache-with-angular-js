package com.redissession.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.redissession.model.RedisSampleObject;

import java.util.Map;
import javax.annotation.PostConstruct;

@Repository
public class RedisRepositoryImpl implements RedisRepository {
    private static final String KEY = "RedisSampleObject";
    
    private RedisTemplate<String, Object> redisTemplate;
    private HashOperations hashOperations;
    
    @Autowired
    public RedisRepositoryImpl(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
    }
    
    public void add(final RedisSampleObject movie) {
        hashOperations.put(KEY, movie.getId(), movie.getName());
    }

    public void delete(final String id) {
        hashOperations.delete(KEY,id);
    }
    
    public RedisSampleObject getRedisSampleObject(final String id){
        return (RedisSampleObject) hashOperations.get(KEY, id);
    }
    
    public Map<Object, Object> getAllRedisSampleObject(){
        return hashOperations.entries(KEY);
    }

    public String getKEY(){
        return KEY;
    }
  
}
