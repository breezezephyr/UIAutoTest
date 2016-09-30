package com.auto.test.selenium;

import com.auto.test.consts.WebDriverConstant;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 实现描述：WebDriver池工厂
 */
@Component
public class WebDriverPoolFactory implements FactoryBean<WebDriverPool> {

    @Autowired
    private WebDriverConstant constant;

    private WebDriverPool pool;

    @Override
    public WebDriverPool getObject() throws Exception {
        return pool;
    }

    @Override
    public Class<?> getObjectType() {
        return WebDriverPool.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @PostConstruct
    public void init() {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMinIdle(constant.getPoolMinIdle());
        config.setMaxIdle(constant.getPoolMaxActive());
        config.setMaxTotal(constant.getPoolMaxActive());
        config.setMaxWaitMillis(30000L);
        config.setTestWhileIdle(true);
        config.setTestOnReturn(true);
        config.setTimeBetweenEvictionRunsMillis(60000L);
        config.setMinEvictableIdleTimeMillis(3600000L);

        pool = new WebDriverPool(new BasePooledObjectFactory<WebDriver>() {
            @Override
            public WebDriver create() throws Exception {
                return createWebDriver();
            }

            @Override
            public PooledObject<WebDriver> wrap(WebDriver obj) {
                return new DefaultPooledObject<>(obj);
            }

            @Override
            public void destroyObject(PooledObject<WebDriver> p) throws Exception {
                destroyWebDriver(p.getObject());
            }

            @Override
            public boolean validateObject(PooledObject<WebDriver> p) {
                return validateWebDriver(p.getObject());
            }
        }, config);
    }

    @PreDestroy
    public void destroy() {
        pool.close();
    }

    private WebDriver createWebDriver() {
        WebDriver driver = new FirefoxDriver(createFirefoxBinary(), createFirefoxProfile());
        driver.manage().window().setSize(new Dimension(constant.getWindowWidth(), constant.getWindowHeight()));
        driver.manage().timeouts().pageLoadTimeout(constant.getPageLoadTimeout(), TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(constant.getImplicitWaitTimeout(), TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(constant.getScriptTimeout(), TimeUnit.SECONDS);
        return driver;
    }

    private FirefoxBinary createFirefoxBinary() {
        String path = System.getProperty("firefox.path");
        if (StringUtils.isBlank(path))
            return new FirefoxBinary();
        return new FirefoxBinary(new File(path));
    }

    private FirefoxProfile createFirefoxProfile() {
        FirefoxProfile profile = new FirefoxProfile();
        // 缓存
        profile.setPreference("browser.cache.disk.enable", true);
        profile.setPreference("browser.cache.memory.enable", true);
        profile.setPreference("browser.cache.offline.enable", true);
        profile.setPreference("network.http.use-cache", true);
        // 浏览器UA
        if (StringUtils.isNotBlank(constant.getUserAgent()))
            profile.setPreference("general.useragent.override", constant.getUserAgent());
        // 支付宝
        profile.setPreference("plugin.state.aliedit", 2);
        profile.setPreference("plugin.state.npalicdo", 2);
        return profile;
    }

    private void destroyWebDriver(WebDriver driver) {
        driver.quit();
    }

    private boolean validateWebDriver(WebDriver driver) {
        driver.get("https://www.baidu.com/");
        return true;
    }

}
