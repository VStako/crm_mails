package com.crm_mails.pages;

import com.crm_mails.models.Letter;
import com.crm_mails.models.UserFactory;
import com.crm_mails.utility.CommonMethods;
import com.crm_mails.utility.WebWindow;
import org.openqa.selenium.*;
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

    @FindBy(xpath = ".//span[text()='Спам']")
    private static WebElement spamEmails;

    @FindBy(xpath = "//div[contains(@class,'messagesTableBody')]//button[contains(@class,'buttonPageRight')]")
    @CacheLookup
    private static WebElement buttonNextPage;

    @FindBy(xpath = "//xhtml:pre")
    private static WebElement textInOriginalLetter;

    @FindBy(xpath = "//div[contains(@class,'messagesTableBody')]//span[@class='mailboxPagesTotal'][2]")
    private static WebElement totalCountOfMails;

    @FindBy(xpath = "//div[contains(@class,'messagesTableBody')]//button[contains(@class,'buttonPageRight uiButtonDisabled')]")
    private static WebElement buttonNextPageDisabled;

    private static final By list = By.xpath(".//div[@class='messagesWrap']/div[contains(@class,'tableRow')]");


    public RamblerPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @Override
    protected BasePage waitForPageToBeLoaded() {
//        waitForElementToBeVisible(ramblerLogo, MIN_WAIT_TIMEOUT);
        return null;
    }

    public void loginToRambler(UserFactory.User user){
        inputLogin.clear();
        inputLogin.sendKeys(user.getEmail());
        inputPassword.clear();
        inputPassword.sendKeys(user.getPassword());
        buttonEnter.click();
    }

    public void goToSpam(){
        spamEmails.click();
    }

    public List<Letter> createListOfLetter() {
        List<Letter> listOfLetters = new ArrayList<Letter>();
        int count = 1;
        Letter letter;
        scrollPage(Scroll.DOWN);
        try {
            if (totalCountOfMails.isDisplayed()) {
                count = (int) Math.ceil(Integer.parseInt(totalCountOfMails.getText()) / 25.00);
            }
        } catch (NoSuchElementException exception){
            System.out.println("NoSuchElementException: " + exception);
        }
        scrollPage(Scroll.UP);
        for (int i = 0; i < count; i++) {
            List<WebElement> webListInPage = driver.findElements(list);
            for (int m = 0; m < 15; m++) {
                letter = createLetter(m);
                if (letter.getSender().equals("") &&  letter.getSubject().equals("")){
                    seeAllList(listOfLetters);
                    return listOfLetters;
                }
                listOfLetters.add(letter);
            }
            scrollPage(Scroll.DOWN);
            for (int n = 15; n < webListInPage.size(); n++) {
                letter = createLetter(n);
                if (letter.getSender().equals("") &&  letter.getSubject().equals("")){
                    seeAllList(listOfLetters);
                    return listOfLetters;
                }
                listOfLetters.add(letter);
            }
            if (buttonNextPage.isDisplayed()) {
                buttonNextPage.click();
            }
            scrollPage(Scroll.UP);
        }
        seeAllList(listOfLetters);
        return listOfLetters;
    }

    private Letter createLetter(int index){
        List<WebElement> webListInPage = driver.findElements(list);
        String sender = webListInPage.get(index).findElement(By.xpath("./a[@class='tableCell tableSenderRow']")).getText();
        String subject = webListInPage.get(index).findElement(By.xpath("./a[@class='tableCell tableSubjectRow']")).getText();
        //get url to original letter
        String href = webListInPage.get(index).findElement(By.xpath("./a[@class='tableCell tableSenderRow']")).getAttribute("href");
        String urlToOriginalLetter = href.replace('#', 'm').substring(0, href.length()) + ".0/raw/id/";
        return new Letter(sender, subject, getBulkId(urlToOriginalLetter));
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

    public void seeAllList(List list){
        for(Object item: list){
            System.out.println(item);
        }
    }

    private void scrollPage(Scroll skroll){
        WebElement bottomEl;
        List<WebElement> webListInPage = driver.findElements(list);
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        WebElement topEl = driver.findElement(By.xpath(".//div[@class='messagesWrap']/div[contains(@class,'tableRow')][1]"));
        if (webListInPage.size() > 25) {
            bottomEl = driver.findElement(By.xpath(".//div[@class='messagesWrap']/div[contains(@class,'tableRow')][25]"));
        } else {
            bottomEl = driver.findElement(By.xpath(".//div[@class='messagesWrap']/div[contains(@class,'tableRow')][" + (webListInPage.size() - 1) + "]"));
        }
        switch (skroll){
            case UP:
                jse.executeScript("arguments[0].scrollIntoView();", topEl);
                break;
            case DOWN:
                jse.executeScript("arguments[0].scrollIntoView();", bottomEl);
                break;
        }
    }

    public enum Scroll{
        UP,
        DOWN

    }
}
