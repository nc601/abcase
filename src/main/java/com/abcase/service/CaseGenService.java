package com.abcase.service;

import com.abcase.entity.Case;
import com.abcase.util.ExcelUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CaseGenService {

    public void interfaceCaseGen(Case icase, File interfaceFile, File caseFile) {
        ExcelUtil obj = new ExcelUtil();
        List<List> list = obj.readExcel(interfaceFile);
        List<Case> allCase = new ArrayList<>();
        int no = 1;
        for (List line : list) {
            List<Case> caseList = genCaseInfo(icase, line, no);
            if (caseList == null) {
                continue;
            }
            allCase.addAll(caseList);
            no++;
        }
        obj.writeExcel(caseFile, allCase);
    }

    private List<Case> genCaseInfo(Case icase, List line, int no) {
        if (line.size() == 6) {
            return null;
        }
        Case oneCase = icase;
        oneCase.setCaseNo(line.get(0).toString() + line.get(1) + no);
        oneCase.setItemName(line.get(0).toString() + line.get(1));
        oneCase.setItemDesc(line.get(4).toString() + "-" + line.get(5).toString());
        oneCase.setCaseNo("2");
        oneCase.setTransactionType("2");
        oneCase.setTestType("功能测试");
        oneCase.setCaseAttr("正向");
        oneCase.setCasePriority(2);
        oneCase.setCasePre("其余字段正常填写");
        List<Case> caseList = getCaseList(oneCase, line);
        return caseList;
    }

    /**
     * 获取测试用例步骤和预期
     *
     * @param oneCase
     * @param line
     */
    private List<Case> getCaseList(Case oneCase, List line) {
        List<Case> caseList = new ArrayList<>();
        String type = line.get(6).toString();//字段类型
        String len = line.get(7).toString();//字段长度
        String required = line.get(8).toString();//是否必输
        String val = "";//取值
        if (line.size() == 10) {
            val = line.get(9).toString();
        }

        if (StringUtils.isNotEmpty(val)) {//字段有规定取值则正向用例取规定值
            String[] valArr = val.split(",");
            for (String v : valArr) {
                if (StringUtils.isNoneEmpty(v)) {
                    Case caseInfo = new Case();
                    copyCaseInfo(caseInfo,oneCase);
                    caseInfo.setCaseStep("1." + line.get(4) + "正常输入，输入值等于" + v + "\n" +
                            "2.post发送。");
                    caseInfo.setCaseAttr("正向");
                    caseInfo.setCaseExpect("响应成功");
                    caseList.add(caseInfo);
                }
            }
        } else {//字段没有限定值，则按字段类型、长度、生成
            if (type.equals("String") || type.equals("char")) {
                Case positiveCaseInfo = new Case();
                copyCaseInfo(positiveCaseInfo, oneCase);
                positiveCaseInfo.setCaseStep("1." + line.get(4) + "正常输入，输入字符长度小于等于" + len + "位\n" +
                        "2.post发送。");
                positiveCaseInfo.setCaseAttr("正向");
                positiveCaseInfo.setCaseExpect("响应成功");
                caseList.add(positiveCaseInfo);
                Case negtiveCaseInfo = new Case();
                copyCaseInfo(negtiveCaseInfo, oneCase);
                negtiveCaseInfo.setCaseStep("1." + line.get(4) + "异常输入，输入字符长度大于" + len + "位\n" +
                        "2.post发送。");
                negtiveCaseInfo.setCaseExpect("响应失败");
                caseList.add(negtiveCaseInfo);
                Case empCaseInfo = new Case();
                copyCaseInfo(empCaseInfo,oneCase);
                empCaseInfo.setCaseStep("1." + line.get(4) + "异常输入，输入空字符\n" +
                        "2.post发送。");
                empCaseInfo.setCaseExpect("响应失败");
                caseList.add(empCaseInfo);
            } else if (type.equals("boolean")) {
                Case positiveCaseInfo = new Case();
                copyCaseInfo(positiveCaseInfo,oneCase);
                positiveCaseInfo.setCaseStep("1." + line.get(4) + "正常输入，输入true\n" +
                        "2.post发送。");
                positiveCaseInfo.setCaseAttr("正向");
                positiveCaseInfo.setCaseExpect("响应成功");
                caseList.add(positiveCaseInfo);
                Case negtiveCaseInfo = new Case();
                copyCaseInfo(negtiveCaseInfo,oneCase);
                negtiveCaseInfo.setCaseStep("1." + line.get(4) + "正常输入，输入false\n" +
                        "2.post发送。");
                negtiveCaseInfo.setCaseExpect("响应成功");
                caseList.add(negtiveCaseInfo);
            } else {
                Case positiveCaseInfo = new Case();
                copyCaseInfo(positiveCaseInfo,oneCase);
                positiveCaseInfo.setCaseStep("1." + line.get(4) + "正常输入，输入数字长度小于等于" + len + "位\n" +
                        "2.post发送。");
                positiveCaseInfo.setCaseAttr("正向");
                positiveCaseInfo.setCaseExpect("响应成功");
                caseList.add(positiveCaseInfo);
                Case negtiveCaseInfo = new Case();
                copyCaseInfo(negtiveCaseInfo,oneCase);
                negtiveCaseInfo.setCaseStep("1." + line.get(4) + "异常输入，输入数字长度大于" + len + "位\n" +
                        "2.post发送。");
                negtiveCaseInfo.setCaseExpect("响应失败");
                caseList.add(negtiveCaseInfo);
                Case empCaseInfo = new Case();
                copyCaseInfo(empCaseInfo,oneCase);
                empCaseInfo.setCaseStep("1." + line.get(4) + "异常输入，输入null\n" +
                        "2.post发送。");
                empCaseInfo.setCaseExpect("响应失败");
                caseList.add(empCaseInfo);
            }
        }
        return caseList;
    }

    /**
     * 复制用例
     *
     * @param caseInfo
     * @param oneCase
     */
    private void copyCaseInfo(Case caseInfo, Case oneCase) {
        caseInfo.setCaseAttr(oneCase.getCaseAttr());
        caseInfo.setCaseNo(oneCase.getCaseNo());
        caseInfo.setCasePre(oneCase.getCasePre());
        caseInfo.setCaseId(oneCase.getCaseId());
        caseInfo.setCasePriority(oneCase.getCasePriority());
        caseInfo.setItemDesc(oneCase.getItemDesc());
        caseInfo.setOwner(oneCase.getOwner());
        caseInfo.setItemName(oneCase.getItemName());
        caseInfo.setSysName(oneCase.getSysName());
        caseInfo.setTestType(oneCase.getTestType());
        caseInfo.setTransactionType(oneCase.getTransactionType());
        caseInfo.setStatus(oneCase.getStatus());
    }


}
