package com.jumkid.activity.config;

import com.jumkid.share.config.AbstractMethodLoggingConfig;
import com.jumkid.share.config.custom.CustomPerformanceMonitorInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Slf4j
@Configuration
@EnableAspectJAutoProxy
@Aspect
public class MethodLoggingConfig extends AbstractMethodLoggingConfig {

    @Override
    @Pointcut("execution(* com.jumkid.activity.repository.ActivityRepository.*(..))" +
            "|| execution(* com.jumkid.activity.repository.PriorityRepository.*(..))" +
            "|| execution(* com.jumkid.activity.repository.ActivityNotificationRepository.*(..))" +
            "|| execution(* com.jumkid.activity.repository.ContentResourceRepository.*(..))")
    public void monitorPointCut() { /* empty method */ }

    @Override
    @Before("execution(* com.jumkid.activity.controller.*Controller.*(..))")
    public void controllerJoinPoint(JoinPoint joinPoint) {
        super.log(joinPoint);
    }

    @Bean
    public Advisor performanceMonitorAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("com.jumkid.activity.config.MethodLoggingConfig.monitorPointCut()");
        return new DefaultPointcutAdvisor(pointcut, new CustomPerformanceMonitorInterceptor(false));
    }

}
