package com.auto.test.service;

import com.google.common.collect.Maps;
import com.auto.test.page.DemoPage;
import com.auto.test.recognize.ImgRecognizer;
import com.auto.test.selenium.Drivers;
import com.auto.test.selenium.WebDriverPool;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

/**
 * @author : sean
 * @version : 1.0.0
 * @since : 9/5/16 11:04 AM
 */
@Service
public class DemoService {

    private String URL = "http://www.xxx.com/";
    @Resource
    private ImgRecognizer imgRecognizer;

    private Map<String, String> inputValue;

    @Resource
    private WebDriverPool webDriverPool;

    @PostConstruct
    public void init() {
        inputValue = Maps.newConcurrentMap();
        inputValue.put("searchStr", "故宫");
        inputValue.put("", "");
    }

    public Map<String, String> demoActions() {

        Map<String, String> logs = Maps.newLinkedHashMap();
        WebDriver webDriver = webDriverPool.borrow();
        webDriver.get(URL);
        this.inputSearchValue(webDriver, logs);
        this.listClick(webDriver, logs);
        webDriverPool.success(webDriver);
        return logs;
    }

    private void inputSearchValue(WebDriver driver, Map<String, String> logs) {
        DemoPage demoPage = Drivers.loadPage(driver, DemoPage.class);
        Drivers.find(driver, demoPage.searchInput).sendKeys("故宫");
        Drivers.find(driver, demoPage.searchButton).click();
        logs.put("inputSearchValue", "搜索");
    }

    private void listClick(WebDriver driver, Map<String, String> logs) {
        Drivers.switchToLastOpenedWindow(driver);
        Drivers.find(driver, By.xpath("//div[@class='sight_in_ticket first']//a[@class='dark_blue']")).click();
        logs.put("listClick", "选择list Item");
    }
}
