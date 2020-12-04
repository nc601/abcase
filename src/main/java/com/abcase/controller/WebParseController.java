package com.abcase.controller;

import com.abcase.service.ParseService;
import com.abcase.util.URLFecter;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面测试用例
 */
@Controller
@RequestMapping("/webcase")
public class WebParseController {


    @Autowired
    private ParseService parseService;

    /**
     * 根据url解析web页面内容
     */
    @RequestMapping("/doParse")
    public String parseContent(String pageUrl, String cookieVal) throws Exception {
        HttpClient client = new DefaultHttpClient();
        //抓取的数据
        String htmlstr = URLFecter.URLParser(client, pageUrl, cookieVal);

        //解析抓取的数据
        Document parse = Jsoup.parse(htmlstr);
        parseService.parseEle(parse, pageUrl);

        return "case";
    }


}
