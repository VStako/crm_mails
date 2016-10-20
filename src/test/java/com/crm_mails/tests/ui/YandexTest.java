package com.crm_mails.tests.ui;

import com.crm_mails.models.UserFactory;
import com.crm_mails.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by stako on 22.09.2016.
 */
public class YandexTest extends BaseTest {
    @BeforeClass
    public void loginPrecondition(){
        openYandexPage();
    }

    @Test
    public void testGetSendingReport(){
        yandexPage.loginToYandex(UserFactory.yandexUser());
        System.out.println("INBOX");
        printList(yandexPage.createListOfLetterFromInbox());
        yandexPage.goToSpam();
        System.out.println("SPAM");
        printList(yandexPage.createListOfLetterFromSpam());
        Assert.assertTrue(true);
    }

    public void printList(List list){
        for(Object letter: list){
            System.out.println(letter);
        }
    }
}
