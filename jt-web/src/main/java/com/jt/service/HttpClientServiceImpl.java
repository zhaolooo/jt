package com.jt.service;

import com.jt.pojo.Item;
import com.jt.util.ObjectMapperUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HttpClientServiceImpl implements HttpClientService{

    @Override
    public List<Item> getItems() {
        List<Item> itemList = new ArrayList<>();
        //1.定义远程访问网址
        String url = "http://manage.jt.com/getItems";
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        try {
           HttpResponse httpResponse = httpClient.execute(httpGet);
           if(httpResponse.getStatusLine().getStatusCode() == 200){
               String result =
                       EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
               //result是jt-manage为jt-web返回的List<Item>的JSON串
               if(!StringUtils.isEmpty(result)){
                   itemList = ObjectMapperUtil.toObj(result, itemList.getClass());
               }
           }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return itemList;
    }
}
