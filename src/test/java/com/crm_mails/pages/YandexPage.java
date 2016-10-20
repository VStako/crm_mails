package com.crm_mails.pages;

import com.crm_mails.models.Letter;
import com.crm_mails.models.UserFactory;
import com.crm_mails.utility.CommonMethods;
import com.crm_mails.utility.WebWindow;
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

    @FindBy(xpath = ".//div[@class='mail-Layout-Inner']//span[text()='Ещё']")
    private static WebElement buttonMore;

    @FindBy(xpath = ".//div[text()='Свойства письма']/..")
    private static WebElement buttonGoToOriginalLetter;

    @FindBy(xpath = "//xhtml:pre")
    private static WebElement textInOriginalLetter;



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
        String bulkId;
        String urlToOriginalLetter;
        for (int i=0; i<webList.size(); i++){
            WebElement webElement = webList.get(i);
            if (isWebElementHasInClassString(webElement.findElement(By.xpath("./span[2]/span[2]/span[1]/span[2]")))){
                count = Integer.parseInt(webElement.findElement(By.xpath("./span[2]/span[2]/span[1]/span[2]")).getText());
            }
            if (count>1){
                for (int j=1; j<=count; j++){
                    sender = webList.get(i+j).findElement(By.xpath("./span[1]/span[2]/span")).getText();
                    subject = webElement.findElement(By.xpath("./span[2]/span[2]/span[1]/span[1]/span")).getText();
                    //get url to original letter
                    String href = webList.get(i+j).findElement(By.xpath("./..")).getAttribute("href");
                    urlToOriginalLetter = href.substring(0, href.length());
                    bulkId = openLetter(urlToOriginalLetter);
                    System.out.println(sender + " - " + bulkId);
                    listOfLetters.add(new Letter(sender, subject, bulkId));
                }
                i=i+count;
                count = 0;
            } else {
                sender = webElement.findElement(By.xpath("./span[1]/span[2]/span")).getText();
                subject = webElement.findElement(By.xpath("./span[2]/span[2]/span[1]/span[1]/span")).getText();
                //get url to original letter
                String href = webElement.findElement(By.xpath("./..")).getAttribute("href");
                urlToOriginalLetter = href.substring(0, href.length());
                bulkId = openLetter(urlToOriginalLetter);
                System.out.println(sender + " - " + bulkId);
                listOfLetters.add(new Letter(sender, subject, bulkId));
            }
        }
        return listOfLetters;
    }

    public List<Letter> createListOfLetterFromSpam(){
        driver.manage().timeouts().implicitlyWait(MAX_WAIT_TIMEOUT, TimeUnit.SECONDS);
        openAllLettersWithSimilarSubject();
        driver.manage().timeouts().implicitlyWait(MAX_WAIT_TIMEOUT, TimeUnit.SECONDS);
        List<WebElement> webList = driver.findElements(list);
        List<Letter> listOfLetters = new ArrayList<Letter>();
        String sender;
        String subject;
        String bulkId;
        String urlToOriginalLetter;
        System.out.println(webList.size());
        for(WebElement webElement: webList){
            sender = webElement.findElement(By.xpath("./span[1]/span[2]/span")).getText();
            subject = webElement.findElement(By.xpath("./span[2]/span[2]/span[1]/span[1]/span")).getText();
            //get url to original letter
            String href = webElement.findElement(By.xpath("./..")).getAttribute("href");
            urlToOriginalLetter = href.substring(0, href.length());
            bulkId = openLetter(urlToOriginalLetter);
            System.out.println(sender + " - " + bulkId);
            listOfLetters.add(new Letter(sender, subject, bulkId));
        }
        return listOfLetters;
    }

    public void openAllLettersWithSimilarSubject(){
        driver.manage().timeouts().implicitlyWait(MAX_WAIT_TIMEOUT, TimeUnit.SECONDS);
        CommonMethods.waitSecond(2);
        while(buttonMoreEmailsHidden.isDisplayed()){
            buttonMoreEmails.click();
        }
        if(buttonGoTop.isDisplayed()){
            buttonGoTop.click();
            CommonMethods.waitSecond(2);
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

    private String openLetter(String url){
        WebWindow windowOfLetter = new WebWindow(driver, url);
        CommonMethods.waitSecond(1);
        buttonMore.click();
        String id = getBulkId(buttonGoToOriginalLetter.getAttribute("href"));
        windowOfLetter.close();
        return id;
    }

    public String getBulkId(String url){
        WebWindow ww = new WebWindow(driver, url);
        CommonMethods.waitSecond(1);
        String str = textInOriginalLetter.getText();
        String[] list = str.split("( |\n)");
        for(int i=0; i<list.length; i++){
            if (list[i].equals("X-Bulk-Id:")){
                ww.close();
                return list[i+1];
            }
        }
        ww.close();
        return "";
    }
}