package com.crm_mails.pages;

import com.crm_mails.models.Letter;
import com.crm_mails.models.UserFactory;
import com.crm_mails.utility.CommonMethods;
import com.crm_mails.utility.WebWindow;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
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

    @FindBy(xpath = ".//*[@id=':2y']")
    private static WebElement promotions;

    @FindBy(xpath = ".//h2[@id=':gr']")
    private static WebElement subjectInOneTypeLetter;

    @FindBy(xpath = "//input[@name='bx_vmb' and @value='1']")
    private static WebElement radiobuttonGroupLetterShutOff;

    @FindBy(xpath = ".//*[text()='Сохранить изменения']")
    private static WebElement saveSettings;

    @FindBy(xpath = ".//*[@id=':5r']//span[3]")
    private static WebElement countOfLetter;

    @FindBy(xpath = "//xhtml:pre")
    private static WebElement textInOriginalLetter;

    @FindBy(xpath = ".//*[@id=':5']/div[2]/div[1]/div/div[1]/div/div/div")
    private static WebElement buttonBackToInbox;

    @FindBy(xpath = ".//*[@id=':5u']")
    private static WebElement buttonNextPage;

    private static final By listInOneTypeLetter = By.xpath(".//div[@role='list']/div");
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
        CommonMethods.waitSecond(1);
        inputPassword.clear();
        inputPassword.sendKeys(user.getPassword());
        buttonEnter.click();
        googleApps.click();
        googleMail.click();
    }

    public List<Letter> createListOfLetter(){
        int index = 71; //Integer.parseInt(countOfLetter.getText());
        System.out.println(index);
        String sender;
        String subject;
        List<Letter> listOfLetters = new ArrayList<Letter>();

        for(int i= 1; i <= index; i++) {
            sender = driver.findElement(By.xpath("//div[@role='main']//table[@id]//tr[" + i + "]//div[2]/span")).getText();
            subject = driver.findElement(By.xpath("//div[@role='main']//table[@id]//tr[" + i + "]//td[6]//span[1]")).getText();
            driver.findElement(By.xpath("//div[@role='main']//table[@id]//tr[" + i + "]")).click();
//            get url to original letter
            String letterID = getLetterId();
            String userID = getClientId();
            String originalLetterUrl = driver.getCurrentUrl().split("#")[0] + "?ui=2&ik=" + userID + "&view=om&th=" + letterID;
            listOfLetters.add(new Letter(sender,subject, getBulkId(originalLetterUrl)));
            buttonBackToInbox.click();
            System.out.println(buttonNextPage.getAttribute("aria-disabled"));
            if (index%50 == 0 && buttonNextPage.getAttribute("aria-disabled").equals("true")) {
                buttonNextPage.click();
            }
        }
        return listOfLetters;
    }

    public String getClientId(){
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        String sText =  jse.executeScript("return GLOBALS[9]").toString();
        return sText;
    }

    private String getLetterId(){
        String[] urlValue = driver.getCurrentUrl().split("/");
        return urlValue[urlValue.length-1];
    }

    private void navigateTo(URL url){
        switch (url){
            case INBOX:
                driver.navigate().to((driver.getCurrentUrl()).split("#")[0] + "#inbox");
                break;
            case SPAM:
                driver.navigate().to((driver.getCurrentUrl()).split("#")[0] + "#spam");
                break;
            case SETTINGS:
                driver.navigate().to((driver.getCurrentUrl()).split("#")[0] + "#settings/general");
                break;
        }
    }

    public void notGroupLettersSettings(){
        navigateTo(URL.SETTINGS);
        if (!CommonMethods.isAttributePresent(radiobuttonGroupLetterShutOff, "checked")){
            radiobuttonGroupLetterShutOff.click();
        }
        saveSettings.click();
        navigateTo(URL.INBOX);
    }

    private enum URL{
        SPAM,
        INBOX,
        SETTINGS
    }


    public String createLetter(String url){
        WebWindow windowOfLetter = new WebWindow(driver, url);
        CommonMethods.waitSecond(1);

        return "";
    }

    public String getBulkId(String url){
        WebWindow ww = new WebWindow(driver, url);
        CommonMethods.waitSecond(1);
        String str = textInOriginalLetter.getText();
        String[] list = str.split("( |\n)");
        for(int i=0; i<list.length; i++){
            if (list[i].equals("X-Bulk-Id:")){
                ww.close();
                System.out.println(list[i+1]);
                return list[i+1];
            }
        }
        ww.close();
        return "";
    }
}
