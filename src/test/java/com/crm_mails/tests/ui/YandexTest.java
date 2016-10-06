package com.crm_mails.tests.ui;

import com.crm_mails.models.UserFactory;
import com.crm_mails.tests.BaseTest;
import com.crm_mails.utility.CommonMethods;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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
        yandexPage.createListOfLetterFromInbox();
        yandexPage.goToSpam();
        yandexPage.createListOfLetterFromSpam();
        Assert.assertTrue(true);
    }

}
