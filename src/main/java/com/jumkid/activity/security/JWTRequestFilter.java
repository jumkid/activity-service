package com.jumkid.activity.security;

import com.jumkid.share.security.BearerTokenRequestFilter;
import com.jumkid.share.security.jwt.TokenUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class JWTRequestFilter extends BearerTokenRequestFilter {

    @Autowired
    public JWTRequestFilter(RestTemplate restTemplate) {
        super(restTemplate);
    }

}
