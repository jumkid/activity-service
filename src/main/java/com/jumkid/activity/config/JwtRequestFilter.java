package com.jumkid.activity.config;

import com.jumkid.activity.exception.JwtExpiredException;
import com.jumkid.activity.exception.JwtTokenNotFoundException;
import com.jumkid.activity.util.jwt.TokenUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String HEADER_AUTHORIZATION = "Authorization";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader(HEADER_AUTHORIZATION);

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                TokenUser tokenUser = new TokenUser(jwtToken);
                username = tokenUser.getUsername();
                log.info("Found jwt token username: {}", username);
            } catch (IllegalArgumentException iae) {
                log.error("Unable to get JWT Token");
            } catch (JwtExpiredException jee) {
                log.info("JWT Token has expired");
                //TODO: renew token if refresh presented
            }
        } else {
            logger.warn("JWT Token is not presented or does not begin with Bearer String");
            throw new JwtTokenNotFoundException();
        }

        // set the token back to response
        response.setHeader(HEADER_AUTHORIZATION, requestTokenHeader);

        filterChain.doFilter(request, response);
    }

}
