package com.answer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * created by liufeng
 * 2020/7/14
 *
 */
@Service
public class ValueAnnotateService {

    @Resource
    private Environment environment;


    public void systemValue(){
        System.out.println("---->"+environment.getProperty("host"));
        System.out.println(environment.getProperty("answer.name"));
    }

}
