package com.jumkid.activity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@Slf4j
@SpringBootApplication
public class ActivityApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(ActivityApplication.class, args);
    }

}
