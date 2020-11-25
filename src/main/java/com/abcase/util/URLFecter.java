package com.abcase.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.util.EntityUtils;


public class URLFecter {
    public static String URLParser(HttpClient client, String url, String cookieVal) throws Exception {
        //用来接收解析的数据
        //获取网站响应的html，这里调用了HTTPUtils类
        HttpResponse response = HTTPUtils.getRawHtml(client, url, cookieVal);
        //获取响应状态码
        int StatusCode = response.getStatusLine().getStatusCode();
        //如果状态响应码为200，则获取html实体内容或者json文件
        String entity = "";
        if (StatusCode == 200) {
            entity = EntityUtils.toString(response.getEntity(), "utf-8");
            EntityUtils.consume(response.getEntity());
        } else {
            //否则，消耗掉实体
            EntityUtils.consume(response.getEntity());
        }
        return entity;
    }
}