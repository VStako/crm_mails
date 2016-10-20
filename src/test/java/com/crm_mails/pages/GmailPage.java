package com.crm_mails.pages;

import com.crm_mails.models.Letter;
import com.crm_mails.models.UserFactory;
import com.crm_mails.utility.CommonMethods;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by stako on 20.10.2016.
 */
public class GmailPage extends BasePage{

    @FindBy(xpath = "//*[@class='logo logo-w']")
    @CacheLookup
    private static WebElement googleLogo;

    @FindBy(xpath = "//input[@id='Email']")
    @CacheLookup
    private static WebElement inputLogin;

    @FindBy(xpath = "//input[@id='next']")
    @CacheLookup
    private static WebElement buttonNext;

    @FindBy(xpath = "//input[@id='Passwd']")
    @CacheLookup
    private static WebElement inputPassword;

    @FindBy(xpath = "//input[@id='signIn']")
    @CacheLookup
    private static WebElement buttonEnter;

    @FindBy(xpath = ".//*[@id='gbwa']/div[1]/a")
    @CacheLookup
    private static WebElement googleApps;

    @FindBy(xpath = ".//*[@id='gb23']/span[1]")
    @CacheLookup
    private static WebElement googleMail;

    @FindBy(xpath = ".//*[@id=':48']//a")
    private static WebElement spamEmails;

    @FindBy(xpath = ".//*[@id=':3y']")
    private static WebElement moreForSpam;

    @FindBy(xpath = ".//*[@id=':2y']")
    private static WebElement promotions;

    @FindBy(xpath = ".//h2[@id=':gr']")
    private static WebElement subjectInOneTypeLetter;

//    private static final By list = By.xpath("//*[@class='yW']/span");
    private static final By listInOneTypeLetter = By.xpath("//*[@class='yW']/span");
    private static final By list = By.xpath(".//*[@id=':36']/tbody/tr/");

    public GmailPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @Override
    protected BasePage waitForPageToBeLoaded() {
        return null;
    }

    public void loginToGmail(UserFactory.User user){
        inputLogin.clear();
        inputLogin.sendKeys(user.getEmail());
        buttonNext.click();
        driver.manage().timeouts().implicitlyWait(MAX_WAIT_TIMEOUT, TimeUnit.SECONDS);
        inputPassword.clear();
        inputPassword.sendKeys(user.getPassword());
        buttonEnter.click();
        googleApps.click();
        googleMail.click();
    }

    public void goToSpam(){
        moreForSpam.click();
        spamEmails.click();
    }

    public List<Letter> createListOfLetter(){
        int index = 35; //Integer.parseInt(driver.findElement(By.xpath(".//*[@id=':ct']/span/b[2]")).getText());
        WebElement letter;
        String sender;
        String subject;
        List<WebElement> listsInOneTypeLetter;
        List<Letter> listOfLetters = new ArrayList<Letter>();

        for(int i= 1; i <= index; i++) {
            letter = driver.findElement(By.xpath(".//*[@id=':36']/tbody/tr[" + index + "]"));
            sender = letter.findElement(By.xpath(".//div[2]/span")).getText();
            subject = letter.findElement(By.xpath("//td[6]//span[1]")).getText();
            letter.click();
            CommonMethods.waitSecond(3);
        }
//        List<WebElement> webListInPage = driver.findElements(list);

//        for (WebElement email: webListInPage) {
////            email.findElement(By.xpath())
//            email.click();
//            listsInOneTypeLetter = driver.findElements(listInOneTypeLetter);
//            for (WebElement letter: listsInOneTypeLetter){
//                letter.click();
//            }
//
//        }

        return listOfLetters;
    }

}
