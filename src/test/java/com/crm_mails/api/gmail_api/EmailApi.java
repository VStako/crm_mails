//package com.crm_mails.api.gmail_api;
//
//import com.evoplay.qatools.framework.handler.Factory;
//import com.evoplay.qatools.framework.utils.Date;
//import com.evoplay.qatools.framework.utils.Utils;
//import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
//import com.google.api.services.gmail.Gmail;
//import com.google.api.services.gmail.model.ListMessagesResponse;
//import com.google.api.services.gmail.model.Message;
//import com.google.api.services.gmail.model.ModifyMessageRequest;
//import com.google.common.collect.ImmutableMap;
//import com.google.common.collect.Maps;
//import org.apache.commons.mail.util.MimeMessageParser;
//import org.hamcrest.Matchers;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.junit.AssumptionViolatedException;
//
//import javax.mail.Session;
//import javax.mail.internet.MimeMessage;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.net.URLDecoder;
//import java.security.GeneralSecurityException;
//import java.util.*;
//
//import static org.junit.Assume.assumeThat;
//
///**
// * Created by stako on 22.03.2017.
// */
//public class EmailApi {
//    private static final String APPLICATION_NAME = "email";
//    private static final String USER = "me";
//    private static String refreshToken() {
//        String url = "https://www.googleapis.com/oauth2/v4/token";
//        LinkedHashMap<String, String> data = Maps.newLinkedHashMap(ImmutableMap.of(
//                "client_id", "",
//                "client_secret", "",
//                "refresh_token", "",
//                "grant_type", "refresh_token"
//        ));
//        //noinspection unchecked
//        LinkedHashMap<String, String> response = (LinkedHashMap) Factory.getHttpUrlHandler().postRequest(url, data);
//        return response.get("access_token");
//    }
//    private static Gmail getService() throws IOException, GeneralSecurityException {
//        String token = refreshToken();
//        GoogleCredential credential = new GoogleCredential().setAccessToken(token);
//        return new Gmail.Builder(
//                GoogleNetHttpTransport.newTrustedTransport(),
//                JacksonFactory.getDefaultInstance(),
//                credential
//        ).setApplicationName(APPLICATION_NAME).build();
//    }
//    private static List<Document> getEmails(String title, long afterTime) throws Exception {
//        Gmail service = getService();
//        String params = "is:unread subject:(" + title + ")";
//        ListMessagesResponse response = service.users().messages().list(USER)
//                .setQ(params)
//                .setIncludeSpamTrash(true)
//                .execute();
//        List<Message> messages = new ArrayList<>();
//        while (response.getMessages() != null) {
//            messages.addAll(response.getMessages());
//            if (response.getNextPageToken() != null) {
//                response = service.users().messages().list(USER)
//                        .setQ(params)
//                        .setIncludeSpamTrash(true)
//                        .execute();
//            } else {
//                break;
//            }
//        }
//        List<Document> emails = new ArrayList<>();
//        ModifyMessageRequest modifyRequest = new ModifyMessageRequest()
//                .setRemoveLabelIds(Collections.singletonList("UNREAD"));
//        for (Message message : messages) {
//            message = service.users().messages().get(USER, message.getId()).setFormat("raw").execute();
//            if (message.getInternalDate() > afterTime) {
//                emails.add(getHtmlContent(message));
//            }
//            service.users().messages().modify(USER, message.getId(), modifyRequest).execute();
//            service.users().messages().trash(USER, message.getId()).execute();
//        }
//        return emails;
//    }
//    private static Element getEmail(String title, long afterTime) {
//        List<Document> emails;
//        long time = Utils.getTime();
//        do {
//            try {
//                emails = getEmails(title, afterTime);
//            } catch (Exception e) {
//                throw new AssumptionViolatedException(e.getMessage(), e.getCause());
//            }
//            if (emails.isEmpty()) {
//                Utils.pause(1);
//            }
//        } while (emails.isEmpty() && Utils.getTime() - time < 15);
//        String error = String.format(
//                "There is no '%s' email received after %s", title, Date.getDate(afterTime, "yyyy-MM-dd HH:mm:ss")
//        );
//        assumeThat(error, emails, Matchers.not(Matchers.empty()));
//        return emails.get(0);
//    }
//    private static Document getHtmlContent(Message message) throws Exception {
//        //noinspection AccessStaticViaInstance
//        ByteArrayInputStream stream = new ByteArrayInputStream(new Base64(true).decodeBase64(message.getRaw()));
//        Session session = Session.getDefaultInstance(new Properties(), null);
//        MimeMessageParser parse = new MimeMessageParser(new MimeMessage(session, stream)).parse();
//        return Jsoup.parse(parse.getHtmlContent());
//    }
//}
