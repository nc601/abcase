package com.abcase.service;

import com.abcase.entity.Case;
import com.abcase.util.ExcelUtil;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CaseGenService {
    private int no = 0;

    public void interfaceCaseGen(Case icase, File interfaceFile, File caseFile) {
        ExcelUtil obj = new ExcelUtil();
        List<List> list = obj.readExcel(interfaceFile);
        if (list == null) {
            return;
        }
        List<Case> allCase = new ArrayList<>();
        int count = 0;
        String field = "";
        for (List line : list) {
            count++;
//            if (!line.contains("input") || !line.contains("out")) {//跳过表头
            if (!line.contains("input")) {//todo 目前只处理input
                continue;
            }
            if (!(line.get(0).equals(field))) { //每个字段用例编号从1开始
                no = 0;
                field = line.get(0).toString();
            }
            System.out.println("第" + count + "行数据:" + JSON.toJSONString(line) + "处理开始");
            List<Case> caseList = genCaseInfo(icase, line);
            if (caseList == null) {
                continue;
            }
            allCase.addAll(caseList);
            System.out.println("第" + count + "行数据处理结束");
        }
        obj.writeExcel(caseFile, allCase);
    }

    private List<Case> genCaseInfo(Case icase, List line) {
        if (line.size() <= 6) {
            return null;
        }
        Case oneCase = icase;
        oneCase.setCaseNo(line.get(0).toString() + "字段验证-");
        oneCase.setItemName(line.get(0).toString() + line.get(1));
        oneCase.setItemDesc(line.get(4).toString() + "-" + line.get(5).toString());
        oneCase.setTransactionType("1-联机");
        oneCase.setTestType("2-功能测试");
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
            if (val.toLowerCase().contains("yymmdd")) {//如果是日期直接生成日期格式的测试用例
                Case caseInfo = new Case();
                copyCaseInfo(caseInfo, oneCase);
                caseInfo.setCaseStep("1." + line.get(4) + "正常输入，输入日期格式为" + val + "\n" +
                        "2.post发送。");
                caseInfo.setCaseAttr("正向");
                caseInfo.setCaseExpect("响应成功");
                caseList.add(caseInfo);
                Case negtiveCaseInfo = new Case();
                copyCaseInfo(negtiveCaseInfo, oneCase);
                negtiveCaseInfo.setCaseStep("1." + line.get(4) + "异常输入，输入非" + val + "格式的字符\n" +
                        "2.post发送。");
                negtiveCaseInfo.setCaseAttr("反向");
                negtiveCaseInfo.setCaseExpect("响应失败");
                caseList.add(negtiveCaseInfo);
                Case negtiveCaseInfo1 = new Case();
                copyCaseInfo(negtiveCaseInfo1, oneCase);
                negtiveCaseInfo1.setCaseStep("1." + line.get(4) + "异常输入，输入字符长度大于" + len + "位\n" +
                        "2.post发送。");
                negtiveCaseInfo1.setCaseAttr("反向");
                negtiveCaseInfo1.setCaseExpect("响应失败");
                caseList.add(negtiveCaseInfo1);
                Case empCaseInfo = new Case();
                copyCaseInfo(empCaseInfo, oneCase);
                empCaseInfo.setCaseStep("1." + line.get(4) + "异常输入，输入空字符\n" +
                        "2.post发送。");
                setExtByRequired(required, empCaseInfo);  //根据是否必输确定输入空字符串的用例属性
                caseList.add(empCaseInfo);
                return caseList;
            }
            String[] valArr = val.split(",");//有默认值，默认值处理
            for (String v : valArr) {
                if (StringUtils.isNoneEmpty(v)) {
                    Case caseInfo = new Case();
                    copyCaseInfo(caseInfo, oneCase);
                    caseInfo.setCaseStep("1." + line.get(4) + "正常输入，输入值等于" + v + "\n" +
                            "2.post发送。");
                    caseInfo.setCaseAttr("正向");
                    caseInfo.setCaseExpect("响应成功");
                    caseList.add(caseInfo);
                }
            }
            Case empCaseInfo = new Case();
            copyCaseInfo(empCaseInfo, oneCase);
            empCaseInfo.setCaseStep("1." + line.get(4) + "异常输入，输入空字符\n" +
                    "2.post发送。");
            setExtByRequired(required, empCaseInfo);  //根据是否必输确定输入空字符串的用例属性
            caseList.add(empCaseInfo);
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
                negtiveCaseInfo.setCaseAttr("反向");
                negtiveCaseInfo.setCaseExpect("响应失败");
                caseList.add(negtiveCaseInfo);
                Case empCaseInfo = new Case();
                copyCaseInfo(empCaseInfo, oneCase);
                empCaseInfo.setCaseStep("1." + line.get(4) + "异常输入，输入空字符\n" +
                        "2.post发送。");
                setExtByRequired(required, empCaseInfo);  //根据是否必输确定输入空字符串的用例属性
                caseList.add(empCaseInfo);
            } else if (type.equals("boolean")) {
                Case positiveCaseInfo = new Case();
                copyCaseInfo(positiveCaseInfo, oneCase);
                positiveCaseInfo.setCaseStep("1." + line.get(4) + "正常输入，输入true\n" +
                        "2.post发送。");
                positiveCaseInfo.setCaseAttr("正向");
                positiveCaseInfo.setCaseExpect("响应成功");
                caseList.add(positiveCaseInfo);
                Case negtiveCaseInfo = new Case();
                copyCaseInfo(negtiveCaseInfo, oneCase);
                negtiveCaseInfo.setCaseStep("1." + line.get(4) + "正常输入，输入false\n" +
                        "2.post发送。");
                negtiveCaseInfo.setCaseAttr("反向");
                negtiveCaseInfo.setCaseExpect("响应失败");
                caseList.add(negtiveCaseInfo);
            } else if ("CLOB".equals(type)) {
                Case positiveCaseInfo = new Case();
                copyCaseInfo(positiveCaseInfo, oneCase);
                positiveCaseInfo.setCaseStep("1." + line.get(4) + "正常输入，输入任意长度字符" + len + "位\n" +
                        "2.post发送。");
                positiveCaseInfo.setCaseAttr("正向");
                positiveCaseInfo.setCaseExpect("响应成功");
                caseList.add(positiveCaseInfo);

                Case empCaseInfo = new Case();
                copyCaseInfo(empCaseInfo, oneCase);
                empCaseInfo.setCaseStep("1." + line.get(4) + "异常输入，输入null或者空字符\n" +
                        "2.post发送。");
                setExtByRequired(required, empCaseInfo);  //根据是否必输确定输入空字符串的用例属性

                caseList.add(empCaseInfo);
            } else {
                Case positiveCaseInfo = new Case();
                copyCaseInfo(positiveCaseInfo, oneCase);
                positiveCaseInfo.setCaseStep("1." + line.get(4) + "正常输入，输入数字长度小于等于" + len + "位\n" +
                        "2.post发送。");
                positiveCaseInfo.setCaseAttr("正向");
                positiveCaseInfo.setCaseExpect("响应成功");
                caseList.add(positiveCaseInfo);
                Case negtiveCaseInfo = new Case();
                copyCaseInfo(negtiveCaseInfo, oneCase);
                negtiveCaseInfo.setCaseStep("1." + line.get(4) + "异常输入，输入数字长度大于" + len + "位\n" +
                        "2.post发送。");
                negtiveCaseInfo.setCaseAttr("反向");
                negtiveCaseInfo.setCaseExpect("响应失败");
                caseList.add(negtiveCaseInfo);
                Case empCaseInfo = new Case();
                copyCaseInfo(empCaseInfo, oneCase);
                empCaseInfo.setCaseStep("1." + line.get(4) + "异常输入，输入null\n" +
                        "2.post发送。");
                setExtByRequired(required, empCaseInfo);  //根据是否必输确定输入空字符串的用例属性
                caseList.add(empCaseInfo);
            }
        }
        return caseList;
    }

    private void setExtByRequired(String required, Case empCaseInfo) {
        if (required.equals("Y")) {
            empCaseInfo.setCaseExpect("响应失败");
            empCaseInfo.setCaseAttr("反向");
        } else {
            empCaseInfo.setCaseExpect("响应成功");
            empCaseInfo.setCaseAttr("正向");
        }
    }

    /**
     * 复制用例
     *
     * @param caseInfo
     * @param oneCase
     */
    private void copyCaseInfo(Case caseInfo, Case oneCase) {
        no++;
        String seqNo = getSeqNo(no);
        caseInfo.setCaseAttr(oneCase.getCaseAttr());
        caseInfo.setCaseNo(oneCase.getCaseNo() + seqNo);
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

    private String getSeqNo(int no) {
        if (no < 10) {
            return "00" + no;
        } else if (no >= 10 && no < 100) {
            return "0" + no;
        } else {
            return "" + no;
        }
    }

}
