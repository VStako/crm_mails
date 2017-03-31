package com.crm_mails.api.gmail_api;


import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.*;
import com.google.api.services.gmail.Gmail;

import java.util.Arrays;
import java.util.List;

/**
 * Created by stako on 13.03.2017.
 */
public class Quickstart {


    /**
     * Application name.
     *
     * Ваш идентификатор клиента     1071174787113-kilmsiskuueu7tj4vfi2hje0qj4jqpnh.apps.googleusercontent.com
     * Ваш секрет клиента    _WgYAVY4D41UCe-9oCHlZ9Ry
     */
    private static final String APPLICATION_NAME = "Gmail API Java Quickstart";
//    private static final String API_KEY = "AIzaSyDKW0t90sem1E5UsIIEZvVTAJMgot8JIMk";


    /**
     * Directory to store user credentials for this application.
     */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/gmail-java-quickstart");

    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     * <p>
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/gmail-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(GmailScopes.GMAIL_LABELS);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.
//        InputStream in = Quickstart.class.getResourceAsStream("C:\\Users\\stako\\IdeaProjects\\crm_mails_note\\src\\test\\resources\\client_secret_1071174787113-kilmsiskuueu7tj4vfi2hje0qj4jqpnh.apps.googleusercontent.com.json");


        String CLIENT_ID = "1071174787113-kilmsiskuueu7tj4vfi2hje0qj4jqpnh.apps.googleusercontent.com";
        String CLIENT_SECRET = "_WgYAVY4D41UCe-9oCHlZ9Ry";
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, CLIENT_ID, CLIENT_SECRET, SCOPES)
                .setDataStoreFactory(DATA_STORE_FACTORY)
                .setAccessType("offline")
                .build();

//        InputStreamReader isr = new InputStreamReader(in);
//        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, isr);

        // Build flow and trigger user authorization request.
//        GoogleAuthorizationCodeFlow flow =
//                new GoogleAuthorizationCodeFlow.Builder(
//                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
//                        .setDataStoreFactory(DATA_STORE_FACTORY)
//                        .setAccessType("offline")
//                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Gmail client service.
     *
     * @return an authorized Gmail client service
     * @throws IOException
     */
    public static Gmail getGmailService() throws IOException {
        String token = refreshToken();
        Credential credential = authorize().setAccessToken(token);
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private static String refreshToken() {
        String url = "https://www.googleapis.com/oauth2/v4/token";
        LinkedHashMap<String, String> data = Maps.newLinkedHashMap(ImmutableMap.of(
                "client_id", "",
                "client_secret", "",
                "refresh_token", "",
                "grant_type", "refresh_token"
        ));

        LinkedHashMap<String, String> response = null;
        try {
            response = (LinkedHashMap) sendPOSTRequst(url, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        LinkedHashMap<String, String> response = (LinkedHashMap) Factory.getHttpUrlHandler().postRequest(url, data);
        return response.get("access_token");
    }

    public static void main(String[] args) throws IOException {
        // Build a new authorized API client service.
        Gmail service = getGmailService();

        // Print the labels in the user's account.
        String user = "me";
        ListLabelsResponse listResponse =
                service.users().labels().list(user).execute();
        List<Label> labels = listResponse.getLabels();
        if (labels.size() == 0) {
            System.out.println("No labels found.");
        } else {
            System.out.println("Labels:");
            for (Label label : labels) {
                System.out.printf("- %s\n", label.getName());
            }
        }

//        getMessage(service);

    }

//    public static void getMessage(Gmail service){
//        GmailApiService gas = new GmailApiService();
//        gas.getBulkId(service);
//    }

    public static LinkedHashMap sendPOSTRequst(String url, LinkedHashMap<String, String> data) throws IOException {
        String urlParameters  = "param1=a&param2=b&param3=c";
        byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
        int    postDataLength = postData.length;
        String request        = "http://example.com/index.php";
        URL url2               = new URL( request );
        HttpURLConnection conn= (HttpURLConnection) url2.openConnection();
        conn.setDoOutput( true );
        conn.setInstanceFollowRedirects( false );
        conn.setRequestMethod( "POST" );
        conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty( "charset", "utf-8");
        conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
        conn.setUseCaches( false );
        try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
            wr.write( postData );
        }
        return (LinkedHashMap)
    }
}
