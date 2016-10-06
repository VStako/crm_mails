package com.crm_mails.tests.ui;

import com.crm_mails.utility.CommonMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by stako on 23.09.2016.
 */
public class TestTest {

    private WebDriver driver;
    private String baseUrl;

    @BeforeMethod
    public void setUp() throws Exception {
        driver = new FirefoxDriver();
        baseUrl = "https://www.google.ru/";
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    @Test
    public void testMy() throws Exception {
        driver.get(baseUrl);
        driver.findElement(By.xpath("//*[@id='ctl00_mnuMainn6']/table/tbody/tr/td[1]/a")).click();
    }

    @AfterMethod
    public void tearDown() throws Exception  {
        driver.quit();
    }
}
