package com.auto.test.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DemoPage extends BasePage {
    Logger logger = LoggerFactory.getLogger(DemoPage.class);
    // Search bar
    @FindBy(xpath = "//input[@id='searchValue']")
    public WebElement searchInput;
    @FindBy(xpath = "//input[@id='searchBtn']")
    public WebElement searchButton;
}
