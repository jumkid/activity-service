package com.jumkid.activity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Configuration
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = false, jsr250Enabled = false)
public class MethodSecurityConfig {
    // void
}
