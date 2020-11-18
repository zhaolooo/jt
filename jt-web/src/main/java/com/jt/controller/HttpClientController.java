package com.jt.controller;

import com.jt.pojo.Item;
import com.jt.service.HttpClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HttpClientController {

    @Autowired
    private HttpClientService httpClientService;
    /**
     * 获取后端manage中的商品数据信息
     */
    @RequestMapping("/getItems")
    public List<Item> getItems(){

        return httpClientService.getItems();
    }
}
