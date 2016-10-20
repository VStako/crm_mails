package com.crm_mails.tests.ui;

import com.crm_mails.models.UserFactory;
import com.crm_mails.tests.BaseTest;
import com.crm_mails.utility.CommonMethods;
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
        gmailPage.loginToGmail(UserFactory.gmailUser());
        gmailPage.createListOfLetter();
        CommonMethods.waitSecond(3);
        Assert.assertTrue(true);
    }

}
