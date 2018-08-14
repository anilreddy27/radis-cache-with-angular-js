package com.redissession.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.redissession.model.RedisSampleObject;

import java.util.Map;
import javax.annotation.PostConstruct;

@Repository
public class RedisRepositoryImpl implements RedisRepository {
    private static final String KEY = "";

    private final Logger LOG = LoggerFactory.getLogger(getClass());

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
    
    public void add(String sessionId, final RedisSampleObject movie) {
        sessionId = sessionId.replaceAll("-","");
        LOG.info("key name ("+KEY+""+sessionId+")");
        hashOperations.put(KEY+""+sessionId, movie.getId(), movie.getName());
    }

    public void delete(String keyName, final String value) {
        hashOperations.delete(keyName, value);
    }
    
    public RedisSampleObject getRedisSampleObject(final String id){
        return (RedisSampleObject) hashOperations.get(KEY, id);
    }
    
    public Map<Object, Object> getAllRedisSampleObject(String keyName){
        keyName = keyName.replaceAll("-","");
        return hashOperations.entries(keyName);
    }

    public String getKEY(){
        return KEY;
    }
  
}
