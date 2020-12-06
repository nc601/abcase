package com.abcase.service;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ParseService {

    String paramStr = "自动化测试";  //默认文本参数
    Integer paramInt = 1; //默认数字参数

    public void parseEle(Document parse, String pageUrl) throws Exception {
        Map<String, List> map = new HashMap<>();
        parseInputTag(parse.getElementsByTag("input"), map);
        parseSelectTag(parse.getElementsByTag("select"), map);
        deriveOperate(pageUrl, map);
    }

    private void parseSelectTag(Elements selects, Map<String, List> map) {
        List<String> tags = new ArrayList<>();
        for (Element ele : selects) {
            String id = ele.attr("id");
            if (id != null) {
                tags.add("id:" + id);
                continue;
            }
            String name = ele.attr("name");
            if (name != null) {
                tags.add("name:" + name);
            }
            map.put("select", tags);
        }
    }

    private void parseInputTag(Elements inputs, Map<String, List> map) {
        List<String> tags = new ArrayList<>();
        for (Element ele : inputs) {
            if ("hidden".equals(ele.attr("type"))) {
                continue;
            }
            if ("text".equals(ele.attr("type")) || ele.attr("type").equals("")) {
                String id = ele.attr("id");
                if (id != null) {
                    tags.add("id:" + id);
                    continue;
                }
                String name = ele.attr("name");
                if (name != null) {
                    tags.add("name:" + name);
                }
            }
            if ("submit".equals(ele.attr("type"))) {
                String id = ele.attr("id");
                if (id != null) {
                    tags.add("op:" + id);
                    continue;
                }
            }
            map.put("input", tags);
        }
    }

    private void deriveOperate(String pageUrl, Map<String, List> map) throws InterruptedException {
        WebDriver driver;
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe");
        driver = new ChromeDriver();

        driver.get(pageUrl + "/");
        Thread.sleep(5000);
        List<String> inputTags = map.get("input");
        for (String temp : inputTags) { //处理input元素
            String type = temp.split(":")[0];
            if ("name".equals(type)) {
                String name = temp.split(":")[1];
                WebElement inputBox = driver.findElement(By.name(name));
                Assert.assertTrue(inputBox.isDisplayed());
                inputBox.sendKeys(paramStr);
                Thread.sleep(5000);
            } else if ("id".equals(type)) {
                String id = temp.split(":")[1];
                WebElement inputBox = driver.findElement(By.id(id));
                Assert.assertTrue(inputBox.isDisplayed());
                inputBox.sendKeys(paramStr);
                Thread.sleep(5000);
            } else {
                String op = temp.split(":")[1];
                driver.findElement(By.id(op)).click();
                Thread.sleep(5000);
            }
        }
        List<String> selectTags = map.get("select");
        for (String temp : selectTags) { //处理select元素
            String type = temp.split(":")[0];
            if ("name".equals(type)) {
                String name = temp.split(":")[1];
                Select sl = (Select) (driver.findElement(By.name(name)));
                sl.selectByIndex(0);
                Thread.sleep(5000);
            } else if ("id".equals(type)) {
                String id = temp.split(":")[1];
                Select sl = (Select) (driver.findElement(By.id(id)));
                sl.selectByIndex(0);
                Thread.sleep(5000);
            } else {
                Thread.sleep(5000);
            }
        }
        driver.quit();
    }

}
