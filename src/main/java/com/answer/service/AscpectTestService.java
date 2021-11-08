package com.answer.service;

import org.springframework.stereotype.Service;

/**
 * created by liufeng
 * 2020/9/10
 */
@Service
public class AscpectTestService {

    public void getException(int num){
        System.out.println(9/num);
    }

}
