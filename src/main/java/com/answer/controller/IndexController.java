package com.answer.controller;

import com.answer.service.ValueAnnotateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * created by liufeng
 * 2020/7/14
 */
@Controller
public class IndexController {

    @RequestMapping("/hello")
    @ResponseBody
    public String index(String name){
        return "hello"+name;
    }

}
