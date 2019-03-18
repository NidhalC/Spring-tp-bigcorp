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
public class BigcorpApplication {


    public static void main(String[] args) {
        SpringApplication.run(BigcorpApplication.class, args);

    }
}

