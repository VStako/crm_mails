package com.crm_mails.utility;

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
}