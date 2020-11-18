package com.jt.web.controller;

import com.jt.pojo.Item;
import com.jt.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class HttpClientController {

    @Autowired
    private ItemService itemService;

    /**
     * url请求地址: http://manage.jt.com/getItems
     */
    @RequestMapping("/getItems")
    public List<Item> getItems(){

        return itemService.getItems();
    }

 }
