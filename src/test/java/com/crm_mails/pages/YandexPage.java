package com.crm_mails.pages;

import com.crm_mails.models.Letter;
import com.crm_mails.models.UserFactory;
import com.crm_mails.utility.CommonMethods;
import com.sun.org.apache.xpath.internal.SourceTree;
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
 * Created by stako on 22.09.2016.
 */
public class YandexPage extends BasePage{

    @FindBy(xpath = "//div[@class='b-stamp']")
    @CacheLookup
    private static WebElement yandexLogo;

    @FindBy(xpath = "//input[@name='login']")
    @CacheLookup
    private static WebElement inputLogin;

    @FindBy(xpath = "//input[@name='passwd']")
    @CacheLookup
    private static WebElement inputPassword;

    @FindBy(xpath = "//button[@type='submit']")
    @CacheLookup
    private static WebElement buttonEnter;

    @FindBy(xpath = ".//span[text()='Входящие']")
    @CacheLookup
    private static WebElement inboxEmails;

    @FindBy(xpath = ".//span[text()='Спам']")
    private static WebElement spamEmails;

    @FindBy(xpath = ".//button[@tabindex='-1']")
    private static WebElement buttonMoreEmails;

    @FindBy(xpath = "//div[contains(@class,'ns-view-messages-pager-load-more ns-view-id-')]") //g-hidden
    private static WebElement buttonMoreEmailsHidden;

    @FindBy(xpath = "//a[contains(@class,'mail-Toolbar-Item_top')]/span")
    private static WebElement buttonGoTop;

    @FindBy(xpath = "//div[contains(@class,'ns-view-container-desc mail-MessagesList js-messages-list')]//div[@class='mail-MessageSnippet-Content']")
    private static List<WebElement> listOfAllEmails;

    private static final By list = By.xpath("//div[contains(@class,'ns-view-container-desc mail-MessagesList js-messages-list')]//div[@class='mail-MessageSnippet-Content']");

    @FindBy(xpath = "//div[contains(@class,'ns-view-container-desc mail-MessagesList js-messages-list')]//div[@class='mail-MessageSnippet-Content']/span[1]/span[2]/span")
    private static WebElement senderName;

    @FindBy(xpath = "//div[contains(@class,'ns-view-container-desc mail-MessagesList js-messages-list')]//div[@class='mail-MessageSnippet-Content']/span[2]/span[2]/span[1]/span[1]/span")
    private static WebElement emailSubject;

    @FindBy(xpath = "//div[contains(@class,'ns-view-container-desc mail-MessagesList js-messages-list')]//div[@class='mail-MessageSnippet-Content']/span[2]/span[2]/span[1]/span[2]")
    private static WebElement emailCountInSubject;


    public YandexPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @Override
    protected BasePage waitForPageToBeLoaded() {
        waitForElementToBeVisible(yandexLogo, MIN_WAIT_TIMEOUT);
        return null;
    }

    public void loginToYandex(UserFactory.User user){
        inputLogin.clear();
        inputLogin.sendKeys(user.getEmail());
        inputPassword.clear();
        inputPassword.sendKeys(user.getPassword());
        buttonEnter.click();
    }

    public void goToInbox(){
        inboxEmails.click();
    }

    public void goToSpam(){
        spamEmails.click();
    }

    public boolean isWebElementHasInClassString(WebElement webEl){
        String str = "mail-MessageSnippet-Item mail-MessageSnippet-Item_threadExpand toggles-Arrow-on-not-folded js-thread-toggle";
        String s = webEl.getAttribute("class");
        return str.length() < s.length();
    }

    public List<Letter> createListOfLetterFromInbox(){
        openAllLettersWithSimilarSubject();
        List<WebElement> webList = driver.findElements(list);
        List<Letter> listOfLetters = new ArrayList<Letter>();
        int count = 0;
        String subject;
        String sender;
        System.out.println(webList.size());
        for (int i=0; i<webList.size(); i++){
            WebElement webElement = webList.get(i);
            if (isWebElementHasInClassString(webElement.findElement(By.xpath("./span[2]/span[2]/span[1]/span[2]")))){
                count = Integer.parseInt(webElement.findElement(By.xpath("./span[2]/span[2]/span[1]/span[2]")).getText());
            }
            if (count>1){
                for (int j=1; j<=count; j++){
                    sender = webList.get(i+j).findElement(By.xpath("./span[1]/span[2]/span")).getText();
                    subject = webElement.findElement(By.xpath("./span[2]/span[2]/span[1]/span[1]/span")).getText();
//                    System.out.println(new Letter(sender, subject, ""));
                    listOfLetters.add(new Letter(sender, subject, ""));
                }
                i=i+count;
                count = 0;
                System.out.println(i);
            } else {
                sender = webElement.findElement(By.xpath("./span[1]/span[2]/span")).getText();
                subject = webElement.findElement(By.xpath("./span[2]/span[2]/span[1]/span[1]/span")).getText();
//                System.out.println(new Letter(sender, subject, ""));
                listOfLetters.add(new Letter(sender, subject, ""));
                System.out.println(i);
            }
        }
        return listOfLetters;
    }

    public void openAllLettersWithSimilarSubject(){
        driver.manage().timeouts().implicitlyWait(MAX_WAIT_TIMEOUT, TimeUnit.SECONDS);
        CommonMethods.waitSecond(2);
        while(buttonMoreEmailsHidden.isDisplayed()){
            buttonMoreEmails.click();
            CommonMethods.waitSecond(2);
        }
        if(buttonGoTop.isDisplayed()){
            buttonGoTop.click();
        }
        List<WebElement> webList = driver.findElements(list);
        driver.manage().timeouts().implicitlyWait(MAX_WAIT_TIMEOUT, TimeUnit.SECONDS);
        for(WebElement we: webList){
            if (isWebElementHasInClassString(we.findElement(By.xpath("./span[2]/span[2]/span[1]/span[2]")))){
                we.findElement(By.xpath("./span[2]/span[2]/span[1]/span[2]")).click();
                CommonMethods.waitSecond(2);
            }
        }
    }

    public List<Letter> createListOfLetterFromSpam(){
        driver.manage().timeouts().implicitlyWait(MAX_WAIT_TIMEOUT, TimeUnit.SECONDS);
        openAllLettersWithSimilarSubject();
        driver.manage().timeouts().implicitlyWait(MAX_WAIT_TIMEOUT, TimeUnit.SECONDS);
        List<WebElement> webList = driver.findElements(list);
        List<Letter> listOfLetters = new ArrayList<Letter>();
        String subject;
        String sender;
        System.out.println(webList.size());
        for(WebElement webElement: webList){
            sender = webElement.findElement(By.xpath("./span[1]/span[2]/span")).getText();
            subject = webElement.findElement(By.xpath("./span[2]/span[2]/span[1]/span[1]/span")).getText();
//            System.out.println(new Letter(sender, subject, ""));
            listOfLetters.add(new Letter(sender, subject, ""));
        }
        return listOfLetters;
    }
}