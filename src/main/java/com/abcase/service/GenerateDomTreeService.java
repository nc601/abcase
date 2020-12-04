package com.abcase.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;


/**
 * 生成dom树Service
 * by llh
 */
@Service
public class GenerateDomTreeService {

    private Jsoup jsoup;

    /**
     * 完整的html解析
     * @param htmlString 完整的html
     * @return Document
     * @throws Exception e
     */
    public Document getDomTreeByString(String htmlString) throws Exception{
        try{
            return jsoup.parse(htmlString);
        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 读取文件解析
     * @param fileRoad 文件路径
     * @return Document
     * @throws Exception e
     */
    public Document getDomTreeByFile(String fileRoad) throws Exception{
        try{
            File input = new File(fileRoad);
            Document doc = jsoup.parse(input,"UTF-8");
            return doc;
        }catch (Exception e){
            throw e;
        }
    }
}
