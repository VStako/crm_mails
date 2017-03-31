package com.crm_mails.tests.ui;

import com.crm_mails.api.gmail_api.GmailApiService;
import com.crm_mails.models.UserFactory;
import com.crm_mails.tests.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Created by stako on 20.10.2016.
 */
public class GmailTest extends BaseTest{

    @BeforeClass
    public void loginPrecondition(){
        openGmailPage();
    }

    @Test
    public void testGetSendingReport(){
//        GmailApiService gmailApi = new GmailApiService();
//        gmailApi.getBulkId();
        gmailPage.loginToGmail(UserFactory.gmailUser());
        gmailPage.getClientId();
        gmailPage.notGroupLettersSettings();
        gmailPage.createListOfLetter();
        Assert.assertTrue(true);
    }
}
