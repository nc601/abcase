package com.abcase.controller;

import com.abcase.entity.Case;
import com.abcase.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 接口测试用例
 */

@Controller
@RequestMapping("/interfaceCase")
public class CaseController {
    @Value("${interfaceFilePath}")
    private String interfaceFilePath;//接口模板文档

    @Value("${caseFilePath}")
    private String caseFilePath;//测试用例文档


    @Value("${systemName}")
    private String systemName;//系统名


    @Value("${designer}")
    private String designer;//用例设计者

    @Autowired
    private CaseService caseService;

    @RequestMapping("/getCase")
    @ResponseBody
    public List<Case> getCase(Map<String, Object> model) {
        List<Case> caseList = caseService.queryCase(new Case());
        return caseList;
    }

    @RequestMapping("/caseInfo")
    public String getCaseInfo(Case caseInfo) {
        caseInfo.setSysName(systemName);
        caseInfo.setOwner(designer);
        caseService.getCaseInfo(caseInfo, new File(interfaceFilePath), new File(caseFilePath));
        return "case";
    }
}
