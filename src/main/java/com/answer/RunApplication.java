package com.answer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * created by liufeng
 * 2018/8/21
 */
@Configuration
@SpringBootApplication
@EnableScheduling
//开启切面
@EnableAspectJAutoProxy
//开启nacos
@EnableDiscoveryClient
public class RunApplication implements ApplicationRunner {
    private static final Logger log=LoggerFactory.getLogger(RunApplication.class);


    public static void main(String[] args) {
        SpringApplication.run(RunApplication.class,args);

    }

    /**
     * springboot启动时自动执行方法
     * @param applicationArguments
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments applicationArguments){
        log.error("run method execute!!!");

    }


}
