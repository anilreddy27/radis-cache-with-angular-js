package com.radissession.controller;

import com.radissession.model.RedisSampleObject;
import com.radissession.repository.RedisRepository;
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
    
    @Autowired
    private RedisRepository redisRepository;

    @RequestMapping("/keys")
    public @ResponseBody Map<Object, Object> keys() {
        return redisRepository.getAllRedisSampleObject();
    }

    @RequestMapping("/values")
    public @ResponseBody Map<String, String> findAll() {

        Date fromDate = new Date();

        Map<Object, Object> aa = redisRepository.getAllRedisSampleObject();

        Map<String, String> map = new HashMap<String, String>();
        int size = 0;
        for(Map.Entry<Object, Object> entry : aa.entrySet()){
            String key = (String) entry.getKey();

            size = size + aa.get(key).toString().length();

            map.put(key, aa.get(key).toString());
        }

        Date responseDate = new Date();

        Long time= responseDate.getTime() - fromDate.getTime();

        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.put("keyName",redisRepository.getKEY());
        resultMap.put("noOfObjects", String.valueOf(map.keySet().size()));
        resultMap.put("noOfObjectInSize", String.valueOf(size/1024));
        resultMap.put("responseTime", String.valueOf(time));

        return resultMap;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<String> add(
        @RequestParam String key,
        @RequestParam String value) {

            StringBuilder sb = new StringBuilder();

            for (int i=0; i<Integer.valueOf(value)*1024; i++) {
                sb.append('a');
            }
            sb.toString();

        RedisSampleObject movie = null;

        for(int i =1;i<=Integer.valueOf(key);i++)
        {
            movie = new RedisSampleObject(UUID.randomUUID().toString(), sb.toString());
            redisRepository.add(movie);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity<String> delete() {
        Map<Object, Object> aa = redisRepository.getAllRedisSampleObject();
        for(Map.Entry<Object, Object> entry : aa.entrySet()){
            String key = (String) entry.getKey();
            redisRepository.delete(key);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
