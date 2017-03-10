package com.crm_mails.tests;

import com.crm_mails.pages.*;
import com.crm_mails.utility.Constant;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

import java.io.File;

/**
 * Created by stako on 14.06.2016.
 */
public class BaseTest {

    protected YandexPage yandexPage;
    protected RamblerPage ramblerPage;
    protected GmailPage gmailPage;
    private WebDriver driver;

    @BeforeClass(alwaysRun = true)
    public void setUp() {
        driver = getWebDriver();
        driver.manage().window().maximize();
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() {
        driver.quit();
    }

    protected void openYandexPage() {
        driver.get(Constant.YANDEX_URL);
        yandexPage = new YandexPage(driver);
    }

    protected void openRamblerPage() {
        driver.get(Constant.RAMBLER_URL);
        ramblerPage = new RamblerPage(driver);
    }

    protected void openGmailPage() {
        driver.get(Constant.GMAIL_URL);
        gmailPage = new GmailPage(driver);
    }

    private WebDriver getWebDriver() {
        if (driver == null) {
//            return new FirefoxDriver(new ProfilesIni().getProfile("WebDriver"));
//            return new FirefoxDriver();
//            File file = new File("c:/Users/stako/IdeaProjects/yhtests/src/test/resources/chromedriver.exe");
//            System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
//            WebDriver driver = new InternetExplorerDriver();
            return getChrome();
        }
        return driver;
    }

    private WebDriver getChrome() {
        File file = new File("./src/test/resources/chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
//        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
//        ChromeOptions options = new ChromeOptions();
//        options.addArguments("test-type");
//        capabilities.setCapability("chrome.binary", "./src/test/resources/chromedriver.exe");
//        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        return new ChromeDriver();
    }
}
