package com.training.springcore.config;

import com.training.springcore.service.SiteService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MonitoredAspect {
    private final static Logger logger = LoggerFactory.getLogger(SiteService.class);

    @Before("@annotation(com.training.springcore.config.Monitored)")
    public void logServiceBeforeCall(JoinPoint jp) {
        System.out.println("Appel finder " + jp.getSignature());
    }
    @AfterReturning(pointcut = "@annotation(com.training.springcore.config.Monitored)", returning = "element")
    public void logServiceAfterCall(JoinPoint jp, Object element){
        if (element== null){
            logger.info("Finder" + jp.getTarget() + "return null");
        }
        else {
            logger.info("Finder" + jp.getTarget() + " COUCOU return " + element.toString());
        }

    }

}
