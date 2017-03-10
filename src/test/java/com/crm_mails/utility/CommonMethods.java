package com.crm_mails.utility;

import org.openqa.selenium.WebElement;

/**
 * Created by stako on 15.06.2016.
 */
public class CommonMethods {

    /**
     * This method pauses execution of code.
     *
     * @param sec number of seconds
     */
    public static void waitSecond(int sec){
        try {
            Thread.sleep(sec*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static boolean isAttributePresent(WebElement element, String attribute) {
        Boolean result = false;
        try {
            String value = element.getAttribute(attribute);
            if (value != null){
                result = true;
            }
        } catch (Exception ignored) {
            System.out.println("CommonMethods.isAttributePresent: " + ignored);
        }
        return result;
    }
}