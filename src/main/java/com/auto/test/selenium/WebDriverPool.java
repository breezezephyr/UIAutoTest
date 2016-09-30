package com.auto.test.selenium;

import com.auto.test.standard.BusinessException;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.AbandonedConfig;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;

/**
 * 实现描述：WebDriver池
 */
public class WebDriverPool implements Closeable {

    private static final Logger logger = LoggerFactory.getLogger(WebDriverPool.class);
    private final ObjectPool<WebDriver> pool;

    public WebDriverPool(PooledObjectFactory<WebDriver> factory, GenericObjectPoolConfig config) {
        AbandonedConfig abandoned = new AbandonedConfig();
        abandoned.setRemoveAbandonedOnMaintenance(true);
        abandoned.setRemoveAbandonedTimeout((int) config.getMinEvictableIdleTimeMillis() / 1000);
        abandoned.setLogAbandoned(true);
        this.pool = new GenericObjectPool<WebDriver>(factory, config, abandoned);
    }

    public WebDriver borrow() {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            throw new BusinessException(10001, e); // 10001 = 创建WebDriver对象异常
        }
    }

    public void success(WebDriver driver) {
        try {
            pool.returnObject(driver);
        } catch (Exception e) {
            logger.warn("could not return driver to the pool", e);
        }
    }

    public void failure(WebDriver driver) {
        try {
            pool.invalidateObject(driver);
        } catch (Exception e) {
            logger.warn("could not return driver to the pool", e);
        }
    }

    @Override
    public void close() {
        pool.close();
    }

}
