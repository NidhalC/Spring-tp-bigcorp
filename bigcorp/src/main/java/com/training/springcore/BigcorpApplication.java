package com.training.springcore;

import com.training.springcore.config.properties.BigCorpApplicationProperties;
import com.training.springcore.service.SiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableConfigurationProperties
public class BigcorpApplication {
    private final static Logger logger = LoggerFactory.getLogger(SiteService.class);

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BigcorpApplication.class, args);

        BigCorpApplicationProperties applicationInfo = context.getBean(BigCorpApplicationProperties.class);
        logger.info("========================================================================");
        logger.info("Application [" + applicationInfo.getName() + "] - version:" + applicationInfo.getVersion());
        logger.info("plus d'informations sur " + applicationInfo.getWebSiteUrl());
        logger.info("========================================================================");
        context.getBean(SiteService.class).findById("test");
    }
}

