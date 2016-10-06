package com.crm_mails.pages;

import com.crm_mails.models.Letter;
import com.crm_mails.models.UserFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stako on 04.10.2016.
 */
public class RamblerPage extends BasePage{

    @FindBy(xpath = "//div[@class='page__promo']")
    @CacheLookup
    private static WebElement ramblerLogo;

    @FindBy(xpath = "//input[@name='login']")
    @CacheLookup
    private static WebElement inputLogin;

    @FindBy(xpath = "//input[@id='form_password']")
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

    @FindBy(xpath = "//div[contains(@class,'messagesTableBody')]//button[contains(@class,'buttonPageRight')]")
    @CacheLookup
    private static WebElement buttonNextPage;

    @FindBy(xpath = "//div[contains(@class,'messagesTableBody')]//span[@class='mailboxPagesTotal'][2]")
    private static WebElement totalCountOfMails;

    @FindBy(xpath = "//div[contains(@class,'messagesTableBody')]//button[contains(@class,'buttonPageRight uiButtonDisabled')]")
    private static WebElement buttonNextPageDisabled;

    private static final By list = By.xpath(".//div[@class='messagesWrap']/div[contains(@class,'tableRow')]");
    private static final By listOfSender = By.xpath(".//div[@class='messagesWrap']//a[@class='tableCell tableSenderRow']");
    private static final By listOfSubject = By.xpath(".//div[@class='messagesWrap']//a[@class='tableCell tableSubjectRow']");


    public RamblerPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @Override
    protected BasePage waitForPageToBeLoaded() {
        waitForElementToBeVisible(ramblerLogo, MIN_WAIT_TIMEOUT);
        return null;
    }

    public void loginToRambler(UserFactory.User user){
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

    private Letter createLetter(int index){
        List<WebElement> webListInPage = driver.findElements(list);
        String sender = webListInPage.get(index).findElement(By.xpath("./a[@class='tableCell tableSenderRow']")).getText();
        String subject = webListInPage.get(index).findElement(By.xpath("./a[@class='tableCell tableSubjectRow']")).getText();
        System.out.println(new Letter(sender, subject));
        return new Letter(sender, subject);
    }

    private void scrollPageTopOrDown(Scroll skroll){
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        WebElement topEl = driver.findElement(By.xpath(".//div[@class='messagesWrap']/div[contains(@class,'tableRow')][1]"));
        WebElement downEl = driver.findElement(By.xpath(".//div[@class='messagesWrap']/div[contains(@class,'tableRow')][25]"));
        switch (skroll){
            case UP:
                jse.executeScript("arguments[0].scrollIntoView();", topEl);
                break;
            case DOWN:
                jse.executeScript("arguments[0].scrollIntoView();", downEl);
                break;
        }
    }

    private enum Scroll{
        UP,
        DOWN
    }

    public List<Letter> createListOfLetter() {
        List<Letter> listOfLetters = new ArrayList<Letter>();
        int count = 1;
        Letter letter;
        scrollPageTopOrDown(Scroll.DOWN);
        if (totalCountOfMails.isDisplayed()) {
            count = (int) Math.ceil(Integer.parseInt(totalCountOfMails.getText()) / 25.00);
        }
        scrollPageTopOrDown(Scroll.UP);
        for (int i = 0; i < count; i++) {
            List<WebElement> webListInPage = driver.findElements(list);
            for (int m = 0; m < 15; m++) {
                letter = createLetter(m);
                if (letter.getSender().equals("") &&  letter.getSubject().equals("")){
                    return listOfLetters;
                }
                listOfLetters.add(letter);
            }
            scrollPageTopOrDown(Scroll.DOWN);
            for (int n = 15; n < webListInPage.size(); n++) {
                letter = createLetter(n);
                if (letter.getSender().equals("") &&  letter.getSubject().equals("")){
                    return listOfLetters;
                }
                listOfLetters.add(letter);
            }
            if (buttonNextPage.isDisplayed()) {
                buttonNextPage.click();
            }
            scrollPageTopOrDown(Scroll.UP);
        }
        return listOfLetters;
    }
}