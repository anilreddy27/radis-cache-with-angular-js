package com.radissession;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class RedisExampleApplication extends WebMvcConfigurerAdapter {
    
    public static void main(String[] args) {
        SpringApplication.run(RedisExampleApplication.class, args);
    }
}
