package com.abcase.controller;

import com.abcase.service.GenerateDomTreeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/testController")
public class testController {
    @Autowired
    GenerateDomTreeService generateDomTreeService;
    /**
     * 读取文件解析
     * @param fileRoad 文件路径
     * @return Document
     * @throws Exception e
     */
    @PostMapping("/getDomTreeByFile")
    public String getDomTreeByFile(@RequestParam String fileRoad) throws Exception{
        try{
            String result = generateDomTreeService.getDomTreeByFile(fileRoad).toString();
            System.out.println(result);
            return result;
        }catch (Exception e){
            throw e;
        }
    }
}
