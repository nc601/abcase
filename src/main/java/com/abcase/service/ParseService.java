package com.abcase.service;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ParseService {

    public void parseEle(Document parse, String pageUrl) throws Exception {
        Elements inputs = parse.getElementsByTag("input"); //获取该页面所有的控件标签
        Elements selects = parse.getElementsByTag("select");
        List<String> ids = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<String> ops = new ArrayList<>();
        for (Element ele : inputs) {
            if ("hidden".equals(ele.attr("type"))) {
                continue;
            }
            if ("text".equals(ele.attr("type")) || ele.attr("type").equals("")) {
                String id = ele.attr("id");
                if (id != null) {
                    ids.add(id);
                    continue;
                }
                String name = ele.attr("name");
                if (name != null) {
                    names.add(name);
                }
            }
            if ("submit".equals(ele.attr("type"))) {
                String id = ele.attr("id");
                if (id != null) {
                    ops.add(id);
                    continue;
                }
            }
        }
        deriveOperate(pageUrl, ids, names, ops);
    }

    private void deriveOperate(String pageUrl, List<String> ids, List<String> names, List<String> ops) throws InterruptedException {
        WebDriver driver;
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe");
        driver = new ChromeDriver();

        driver.get(pageUrl + "/");
        Thread.sleep(5000);
        for (String id : ids) {
            WebElement inputBox = driver.findElement(By.id(id));
            Assert.assertTrue(inputBox.isDisplayed());
            inputBox.sendKeys("软件自动化测试");
            Thread.sleep(5000);
            driver.findElement(By.id("su")).click();
            Thread.sleep(5000);
        }
        for (String name : names) {
            WebElement inputBox = driver.findElement(By.name(name));
            Assert.assertTrue(inputBox.isDisplayed());
            inputBox.sendKeys("软件自动化测试");
            Thread.sleep(5000);
        }
        for (String op : ops) {
            driver.findElement(By.id(op)).click();
            Thread.sleep(5000);
        }
        driver.quit();
    }

}
