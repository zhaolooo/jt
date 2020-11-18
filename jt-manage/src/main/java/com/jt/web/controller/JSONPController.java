package com.jt.web.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.ItemDesc;
import com.jt.util.ObjectMapperUtil;
import jdk.nashorn.internal.runtime.regexp.JoniRegExp;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JSONPController {

    /**
     * 实现JSONP跨域请求
     * url地址: http://manage.jt.com/web/testJSONP?callback=xxxxxx
     * 参数:    暂时没有
     * 返回值:  callback(JSON);
     */
    /* @RequestMapping("/web/testJSONP")
     public String testJSONP(String callback){
         ItemDesc itemDesc = new ItemDesc();
         itemDesc.setItemId(1000L).setItemDesc("JSONP测试!!!");
         String json = ObjectMapperUtil.toJSON(itemDesc);
         return callback+"("+json+")";
     }*/

    @RequestMapping("/web/testJSONP")
    public JSONPObject testJSONP(String callback){
        ItemDesc itemDesc = new ItemDesc();
        itemDesc.setItemId(1000L).setItemDesc("JSONP测试!!!");
        return new JSONPObject(callback, itemDesc);
    }
}
