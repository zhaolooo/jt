package com.jt;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class HttpClientTest {

    /**
     * 要求:在java代码内部,获取百度的页面.
     * 实现步骤:
     *  1.确定目标地址: https://www.baidu.com/
     *  2.创建httpClient客户端对象
     *  3.创建请求类型
     *  4.发起http请求.并且获取响应的结果.之后判断状态码是否为200 如果等于200则请求正确
     *  5.如果请求正确则动态获取响应值信息.之后进行数据的再次加工....
     *  */
    @Test
    public void testGet() throws IOException {
        String url = "https://www.jd.com/";
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        if(httpResponse.getStatusLine().getStatusCode() == 200) {
            //表示用户请求正确
            //获取返回值数据
            HttpEntity httpEntity = httpResponse.getEntity();
            String result = EntityUtils.toString(httpEntity, "UTF-8");
            System.out.println(result);
            // ...之后做后续的处理
        }
    }
}
