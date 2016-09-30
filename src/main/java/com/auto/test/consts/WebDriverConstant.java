package com.auto.test.consts;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 实现描述：WebDriver常量
 *
 */
@Component
@ConfigurationProperties(prefix = "webdriver")
public class WebDriverConstant {

    private int poolMinIdle;
    private int poolMaxActive;
    private int pageLoadTimeout;
    private int implicitWaitTimeout;
    private int scriptTimeout;
    private int windowWidth;
    private int windowHeight;
    private String userAgent;

    public int getPoolMinIdle() {
        return poolMinIdle;
    }

    public void setPoolMinIdle(int poolMinIdle) {
        this.poolMinIdle = poolMinIdle;
    }

    public int getPoolMaxActive() {
        return poolMaxActive;
    }

    public void setPoolMaxActive(int poolMaxActive) {
        this.poolMaxActive = poolMaxActive;
    }

    public int getPageLoadTimeout() {
        return pageLoadTimeout;
    }

    public void setPageLoadTimeout(int pageLoadTimeout) {
        this.pageLoadTimeout = pageLoadTimeout;
    }

    public int getImplicitWaitTimeout() {
        return implicitWaitTimeout;
    }

    public void setImplicitWaitTimeout(int implicitWaitTimeout) {
        this.implicitWaitTimeout = implicitWaitTimeout;
    }

    public int getScriptTimeout() {
        return scriptTimeout;
    }

    public void setScriptTimeout(int scriptTimeout) {
        this.scriptTimeout = scriptTimeout;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

}
