package com.redissession.config;

import org.springframework.cloud.service.ServiceInfo;
import org.springframework.cloud.service.UriBasedServiceInfo;

@ServiceInfo.ServiceLabel("redis")
public class RedisServiceInfo extends UriBasedServiceInfo {

    public static final String REDIS_SCHEMA = "redis";
    public RedisServiceInfo(String id, String scheme, String host, int port, String username, String password, String path) {
        super(id, REDIS_SCHEMA, host, port, username, password, null);
    }

    public RedisServiceInfo(String id, String uriString) {
        super(id, uriString);
    }
}