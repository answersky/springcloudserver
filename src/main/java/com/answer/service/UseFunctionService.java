package com.answer.service;

/**
 * created by liufeng
 * 2018/9/12
 */
public class UseFunctionService {
    private FunctinonService functinonService;

    public void setFunctinonService(FunctinonService functinonService) {
        this.functinonService = functinonService;
    }

    public void say(){
        functinonService.function();
    }
}
