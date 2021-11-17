package com.answer.controller;

import com.answer.model.Img;
import com.answer.service.ValueAnnotateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
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

    @RequestMapping("/img")
    @ResponseBody
    public String img(@RequestBody Img img){
        System.out.println(img.getBase64Str());
        return "1";
    }

}
