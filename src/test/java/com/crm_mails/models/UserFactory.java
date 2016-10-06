package com.crm_mails.models;

import com.crm_mails.utility.Constant;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by stako on 14.07.2016.
 */
public abstract class UserFactory {
    private static final String RESOURCES_CREDENTIALS_PATH = "src/test/resources/credentials.properties";

    /**
     * Creates and returns valid user
     *
     * @return valid {@code User} object
     * @see #readFromProperties(com.crm_mails.models.UserFactory.MailProvider)
     */
    public static User yandexUser() {
        return readFromProperties(MailProvider.YANDEX);
    }

    /**
     * Creates and returns user with invalid email
     *
     * @return invalid {@code User} object
     * @see #readFromProperties(com.crm_mails.models.UserFactory.MailProvider)
     */
    public static User mailUser() {
        return readFromProperties(MailProvider.MAIL);
    }

    /**
     * Creates and returns user with invalid password
     *
     * @return invalid {@code User} object
     * @see #readFromProperties(com.crm_mails.models.UserFactory.MailProvider)
     */
    public static User gmailUser() {
        return readFromProperties(MailProvider.GMAIL);
    }

    /**
     * Creates and returns user with empty email
     *
     * @return invalid {@code User} object
     * @see #readFromProperties(com.crm_mails.models.UserFactory.MailProvider)
     */
    public static User hotmailUser() {
        return readFromProperties(MailProvider.HOTMAIL);
    }

    /**
     * Creates and returns user with empty password
     *
     * @return invalid {@code User} object
     * @see #readFromProperties(com.crm_mails.models.UserFactory.MailProvider)
     */
    public static User raamblerUser() {
        return readFromProperties(MailProvider.RAMBLER);
    }

    /**
     * Creates and returns user with empty password
     *
     * @return invalid {@code User} object
     * @see #readFromProperties(com.crm_mails.models.UserFactory.MailProvider)
     */
    public static User yahooUser() {
        return readFromProperties(MailProvider.YAHOO);
    }

    /**
     * Reads user's credentials from property file and creates appropriate user
     *
     * @param mailProvider current case's of email provider
     * @return {@code User} object
     * @see com.crm_mails.models.UserFactory.MailProvider
     * @see #RESOURCES_CREDENTIALS_PATH
     */
    private static User readFromProperties(MailProvider mailProvider) {
        Properties prop = new Properties();
        try (InputStream input = new FileInputStream(RESOURCES_CREDENTIALS_PATH)) {
            prop.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read properties", e);
        }
        String email = prop.getProperty("yandexEmail");
        String password = prop.getProperty("yandexPassword");
        switch (mailProvider) {
            case YANDEX:
                email = prop.getProperty("yandexEmail");
                password = prop.getProperty("yandexPassword");
                break;
            case MAIL:
                email = prop.getProperty("mailEmail");
                password = prop.getProperty("mailPassword");
                break;
            case GMAIL:
                email = prop.getProperty("gmailEmail");
                password = prop.getProperty("gmailPassword");
                break;
            case RAMBLER:
                email = prop.getProperty("ramblerEmail");
                password = prop.getProperty("remblerPassword");
                break;
            case HOTMAIL:
                email = prop.getProperty("hotmailEmail");
                password = prop.getProperty("hotmailPassword");
                break;
            case YAHOO:
                email = prop.getProperty("yahooEmail");
                password = prop.getProperty("yahooPassword");
                break;
        }
        return new User(email, password);
    }

    /**
     * Inner class representing user
     */
    public static class User {
        private String email;
        private String password;

        private User(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }

    /**
     * Enum representing cases' validity
     */
    private enum MailProvider {
        YANDEX,
        MAIL,
        GMAIL,
        RAMBLER,
        HOTMAIL,
        YAHOO
    }
}
