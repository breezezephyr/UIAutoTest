package com.auto.test.selenium;

import com.auto.test.utils.Threads;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.auto.test.page.BasePage;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;

/**
 * 实现描述：selenium工具类
 */
public abstract class Drivers {

    private static final Logger logger = LoggerFactory.getLogger(Drivers.class);
    private static final int DEFAULT_EXPLICIT_WAIT_TIMEOUT = 20;

    public static <V> V wait(WebDriver driver, Function<WebDriver, V> isTrue) {
        return new WebDriverWait(driver, DEFAULT_EXPLICIT_WAIT_TIMEOUT).until(isTrue);
    }

    @SuppressWarnings("unchecked")
    public static <V> V wait(WebDriver driver, Function<WebDriver, V> isTrue, long sleepInMillis,
            Class<?>... ignoringExceptionTypes) {
        List<Class<? extends Throwable>> types = Lists.newArrayList();
        for (Class<?> ignoringExceptionType : ignoringExceptionTypes)
            types.add((Class<? extends Throwable>) ignoringExceptionType);
        return new WebDriverWait(driver, DEFAULT_EXPLICIT_WAIT_TIMEOUT, sleepInMillis).ignoreAll(types).until(isTrue);
    }

    public static Optional<WebElement> exists(WebDriver driver, By by) {
        List<WebElement> elems = driver.findElements(by);
        if (elems != null && !elems.isEmpty() && elems.get(0).isDisplayed())
            return Optional.of(elems.get(0));
        return Optional.empty();
    }

    public static WebElement find(WebDriver driver, By by) {
        return wait(driver, ExpectedConditions.presenceOfElementLocated(by));
    }

    /**
     * TODO 异常处理Page页元素不能正常加载,不要每次都要去判断页面元素是否present
     */

    public static <T extends BasePage> T loadPage(WebDriver driver, Class<T> clazz) {

        T basePage = PageFactory.initElements(driver, clazz);

        //        if (basePage.element.isDisplayed())
        return basePage;

        //        wait(driver, ExpectedConditions.visibilityOf(basePage.element));
    }

    public static WebElement find(WebDriver driver, WebElement webElement) {
        return wait(driver, ExpectedConditions.visibilityOf(webElement));
    }

    public static List<WebElement> finds(WebDriver driver, By by) {
        return wait(driver, ExpectedConditions.presenceOfAllElementsLocatedBy(by));
    }

    public static WebElement visible(WebDriver driver, By by) {
        return wait(driver, ExpectedConditions.visibilityOfElementLocated(by));
    }

    public static void untilVisible(WebDriver driver, final WebElement elem) {
        wait(driver, new Function<WebDriver, Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return elem.isDisplayed();
            }
        });
        Threads.sleep(500L);
    }

    public static void untilInvisible(WebDriver driver, final WebElement elem) {
        wait(driver, new Function<WebDriver, Boolean>() {
            @Override
            public Boolean apply(WebDriver input) {
                return !elem.isDisplayed();
            }
        });
        Threads.sleep(500L);
    }

    public static void sendKeys(WebDriver driver, By by, String keys) {
        WebElement input = find(driver, by);
        input.clear();
        input.sendKeys(keys);
    }

    public static void sendKeys(WebElement input, String keys) {
        input.clear();
        input.sendKeys(keys);
    }

    public static void acceptAlert(WebDriver driver) {
        try {
            driver.switchTo().alert().accept();
        } catch (NoAlertPresentException e) {
            logger.info("no alert here {}", driver.getTitle());
        }
    }

    public static File screenshot(WebDriver driver) {
        try {
            return ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        } catch (WebDriverException e) {
            logger.warn("selenium screenshot failure", e);
            return null;
        }
    }

    public static File screenshot(WebDriver driver, int x, int y, int w, int h) {
        try {
            File screenshot = screenshot(driver);
            if (screenshot != null) {
                BufferedImage elemImg = ImageIO.read(screenshot).getSubimage(x, y, w, h);
                ImageIO.write(elemImg, "png", screenshot);
                return screenshot;
            }
        } catch (Exception e) {
            logger.warn("selenium sceenshot failure", e);
        }
        return null;
    }

    public static File screenshot(WebDriver driver, WebElement element) {
        // 切图指定元素
        int x = element.getLocation().getX(), y = element.getLocation().getY();
        int w = element.getSize().getWidth(), h = element.getSize().getHeight();
        return screenshot(driver, x, y, w, h);
    }

    @SuppressWarnings("unchecked")
    public static <T> T script(WebDriver driver, String script, Object... args) {
        return (T) ((JavascriptExecutor) driver).executeScript(script, args);
    }

    public static WebElement self(WebDriver driver, WebElement anchor) {
        script(driver, "arguments[0].setAttribute('target','_self')", anchor);
        return anchor;
    }


    public static void mouseHover(WebDriver driver, WebElement element) {
        new Actions(driver).moveToElement(element).perform();
        sleep(1000);
    }

    public static void mouseHover(WebDriver driver, String xpath) {
        new Actions(driver).moveToElement(driver.findElement(By.xpath(xpath))).perform();
        sleep(1000);
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void switchToLastOpenedWindow(WebDriver driver) {
        String lastWindow = null;
        for (String window : driver.getWindowHandles()) {
            lastWindow = window;
        }
        driver.switchTo().window(lastWindow);
    }

}
