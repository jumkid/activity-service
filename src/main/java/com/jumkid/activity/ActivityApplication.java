package com.jumkid.activity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@Slf4j
@SpringBootApplication
public class ActivityApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ActivityApplication.class, args);
    }

    @Bean(name = "restTemplate")
    public RestTemplate restTemplateBean(){
        return new RestTemplate();
    }

}
