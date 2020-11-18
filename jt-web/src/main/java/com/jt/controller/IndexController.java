package com.jt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller     //跳转页面
public class IndexController {

    //用户通过 /index请求实现页面的跳转
    @RequestMapping("/index")
    public String index(){

        return "index";
    }


}
