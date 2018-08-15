package com.redissession.controller;

import com.redissession.model.RedisSampleObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;


@RestController
@RequestMapping("/")
public class WebController {

    @Autowired
    private Environment environment;

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @RequestMapping("/values")
    public @ResponseBody Map<String, String> findAll(@RequestParam(required = false)String keyName, HttpSession session) {

        LOG.info("Begin - "+session.getId());
        Map<String, String> resultMap = new HashMap<String, String>();
        //Date fromDate = new Date();
        if(!session.isNew())
        {
            int size = 0;

            List<RedisSampleObject> redisSampleObjects = (List<RedisSampleObject>) session.getAttribute("testObjectForSession");
            for(RedisSampleObject redisSampleObject : redisSampleObjects)
            {
                size = size + redisSampleObject.getName().toString().length();
            }

            resultMap.put("noOfObjects", String.valueOf(redisSampleObjects.size()));
            resultMap.put("noOfObjectInSize", String.valueOf(size/1024));

        }
        Long creationTime = (Long)session.getAttribute("CreationTime");
        if(creationTime == null)
            creationTime = session.getCreationTime();

        Long time = System.currentTimeMillis()-creationTime;
        resultMap.put("keyName",session.getId());
        resultMap.put("responseTime", String.valueOf(time));
        return resultMap;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public @ResponseBody Map<String, String> add(
        @RequestParam String key,
        @RequestParam String value, HttpSession session) {

        LOG.info("Begin - "+session.getId());

        Map<String, String> resultMap = new HashMap<String, String>();
        List<RedisSampleObject> redisSampleObjectList = new ArrayList<>();

        if(session.isNew())
        {
            String instanceId = environment.getProperty("vcap.application.instance_id");
            session.setAttribute("InstanceIDWhenCreated",instanceId);
            session.setAttribute("instanceId",instanceId);

            createTestObject(key, value, session, redisSampleObjectList);

        }else{
           /* List<RedisSampleObject> redisSampleObjects = (List<RedisSampleObject>) session.getAttribute("testObjectForSession");
            if(redisSampleObjects == null)
            {
                redisSampleObjects = new ArrayList<>();
            }*/
            createTestObject(key, value, session, redisSampleObjectList);
        }

        session.setAttribute("SessionId",session.getId());
        session.setAttribute("IsNew",session.isNew());
        session.setAttribute("MaxInactiveInterval",session.getMaxInactiveInterval());
        session.setAttribute("CreationTime",session.getCreationTime());

        Long time = System.currentTimeMillis()-(Long)session.getAttribute("CreationTime");
        resultMap.put("keyName",session.getId());
        resultMap.put("responseTime", String.valueOf(time));
        return resultMap;
    }

    private void createTestObject(@RequestParam String key, @RequestParam String value, HttpSession session, List<RedisSampleObject> redisSampleObjectList) {

        StringBuilder sb = new StringBuilder();

        for (int i=0; i<Integer.valueOf(value)*1024; i++) {
            sb.append('a');
        }

        RedisSampleObject movie = null;

        for(int i =1;i<=Integer.valueOf(key);i++)
        {
            movie = new RedisSampleObject(UUID.randomUUID().toString(), sb.toString());
            redisSampleObjectList.add(movie);
        }
        session.setAttribute("testObjectForSession",redisSampleObjectList);
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ResponseEntity<String> delete(@RequestParam(required = false)String keyName,HttpSession session) {
        if(!session.isNew())
        {
            session.invalidate();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
