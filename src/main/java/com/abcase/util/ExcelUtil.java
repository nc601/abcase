package com.abcase.util;

import com.abcase.entity.Case;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {
    public static void main(String[] args) {
        ExcelUtil obj = new ExcelUtil();
        // 此处为我创建Excel路径：E:/template.xls下
        File file = new File("E:/template.xls");
        List excelList = obj.readExcel(file);
        System.out.println("list中的数据打印出来");
        for (int i = 0; i < excelList.size(); i++) {
            List list = (List) excelList.get(i);
            for (int j = 0; j < list.size(); j++) {
                System.out.print(list.get(j));
            }
            System.out.println();
        }

    }

    /**
     * 读取excel文档
     *
     * @param file
     * @return
     */
    public List readExcel(File file) {
        try {
            // 创建输入流，读取Excel
            InputStream is = new FileInputStream(file);
            // jxl提供的Workbook类
            Workbook wb = Workbook.getWorkbook(is);
            // Excel的页签数量
            int sheet_size = wb.getNumberOfSheets();
            for (int index = 0; index < sheet_size; index++) {
                List<List> outerList = new ArrayList<List>();
                // 每个页签创建一个Sheet对象
                Sheet sheet = wb.getSheet(index);
                // sheet.getRows()返回该页的总行数
                for (int i = 0; i < sheet.getRows(); i++) {
                    List innerList = new ArrayList();
                    // sheet.getColumns()返回该页的总列数
                    for (int j = 0; j < sheet.getColumns(); j++) {
                        String cellinfo = sheet.getCell(j, i).getContents();
                        if (cellinfo.isEmpty()) {
                            continue;
                        }
                        innerList.add(cellinfo);
                        System.out.print(cellinfo);
                    }
                    outerList.add(i, innerList);
                    System.out.println();
                }
                return outerList;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 写excel文档
     *
     * @param file
     * @param caseList
     */
    public void writeExcel(File file, List<Case> caseList) {
        if (file.exists()) {
            //如果文件存在就删除
            file.delete();
        }
        try {
            file.createNewFile();
            //创建工作簿
            WritableWorkbook workbookA = Workbook.createWorkbook(file);
            //创建sheet
            WritableSheet sheetA = workbookA.createSheet("sheet1", 0);
            Label labelA = null;
            String[] title = new String[]{"业务领域（系统英文简称）*", "测试用例编号*", "需求名称*", "用例描述*", "用例优先级*", "交易类型*", "测试类型", "用例属性*", "前置条件", "[步骤]名称", "[步骤]预期", "关联用例编号", "ATP编号", "ATP执行方式", "设计者*"};
            //设置列名
            for (int i = 0; i < title.length; i++) {
                labelA = new Label(i, 0, title[i]);
                sheetA.addCell(labelA);
            }
            //将测试用例写入文件中
            for (int i = 1; i < caseList.size(); i++) {
                String[] itemArr = covertObjectToArray(caseList.get(i), title.length);
                for (int j = 0; j < title.length; j++) {
                    labelA = new Label(j, i, itemArr[j]);
                    sheetA.addCell(labelA);
                }
            }
            workbookA.write();    //写入数据
            workbookA.close();  //关闭连接

        } catch (Exception e) {
            System.out.println("文件写入失败，报异常...");
        }

    }

    private String[] covertObjectToArray(Case aCase, int len) {
        String[] itemArr = new String[]{aCase.getSysName(), aCase.getCaseNo(), aCase.getItemName(), aCase.getItemDesc(), aCase.getCasePriority() + "", aCase.getTransactionType(), aCase.getTestType(), aCase.getCaseAttr(), aCase.getCasePre(), aCase.getCaseStep(), aCase.getCaseExpect(), "", "", "", aCase.getOwner()};
        return itemArr;
    }

}