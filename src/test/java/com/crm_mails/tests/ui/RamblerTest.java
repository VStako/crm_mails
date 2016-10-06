package com.crm_mails.tests.ui;

import com.crm_mails.models.UserFactory;
import com.crm_mails.tests.BaseTest;
import com.crm_mails.utility.CommonMethods;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Created by stako on 05.10.2016.
 */
public class RamblerTest extends BaseTest {

    @BeforeClass
    public void loginPrecondition(){
        openRamblerPage();
    }

    @Test
    public void testGetSendingReport(){
        ramblerPage.loginToRambler(UserFactory.raamblerUser());
        ramblerPage.createListOfLetter();
        ramblerPage.goToSpam();
        CommonMethods.waitSecond(3);
        ramblerPage.createListOfLetter();
        Assert.assertTrue(true);
    }

}
