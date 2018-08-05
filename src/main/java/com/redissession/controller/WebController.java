package com.redissession.controller;

import com.redissession.model.RedisSampleObject;
import com.redissession.repository.RedisRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/")
public class WebController {

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    private RedisRepository redisRepository;

    @RequestMapping("/keys")
    public @ResponseBody Map<Object, Object> keys(@RequestParam(required = false)String keyName) {

        return redisRepository.getAllRedisSampleObject(keyName);
    }

    @RequestMapping("/values")
    public @ResponseBody Map<String, String> findAll(@RequestParam(required = false)String keyName) {

        Date fromDate = new Date();

        Map<Object, Object> aa = redisRepository.getAllRedisSampleObject(keyName);

        Map<String, String> map = new HashMap<String, String>();
        int size = 0;
        for(Map.Entry<Object, Object> entry : aa.entrySet()){
            String key = (String) entry.getKey();

            size = size + aa.get(key).toString().length();

            map.put(key, aa.get(key).toString());
        }

        Date responseDate = new Date();

        LOG.info("response time : ");
        Long time= responseDate.getTime() - fromDate.getTime();

        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("keyName",keyName);
        resultMap.put("noOfObjects", String.valueOf(map.keySet().size()));
        resultMap.put("noOfObjectInSize", String.valueOf(size/1024));
        resultMap.put("responseTime", String.valueOf(time));

        LOG.info("response time(in milli seconds) : "+String.valueOf(time));

        return resultMap;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody Map<String, String> add(
        @RequestParam String key,
        @RequestParam String value) {

            StringBuilder sb = new StringBuilder();

            for (int i=0; i<Integer.valueOf(value)*1024; i++) {
                sb.append('a');
            }
            sb.toString();

        RedisSampleObject movie = null;
        String sessionId = UUID.randomUUID().toString();

        for(int i =1;i<=Integer.valueOf(key);i++)
        {
            movie = new RedisSampleObject(UUID.randomUUID().toString(), sb.toString());
            redisRepository.add(sessionId,movie);
        }
         Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("keyName",redisRepository.getKEY()+"_"+sessionId);
        return resultMap;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity<String> delete(@RequestParam(required = false)String keyName) {
        Map<Object, Object> aa = redisRepository.getAllRedisSampleObject(keyName);
        for(Map.Entry<Object, Object> entry : aa.entrySet()){
            String key = (String) entry.getKey();
            redisRepository.delete(keyName,key);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
