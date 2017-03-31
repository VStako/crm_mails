//package com.crm_mails.api.gmail_api;
//
//import com.crm_mails.models.UserFactory;
//import com.google.api.services.gmail.Gmail;
//
//import com.google.api.services.gmail.model.Message;
//import com.google.gson.Gson;
//import com.google.gson.JsonElement;
//import com.google.gson.JsonObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.util.Arrays;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * Created by stako on 13.03.2017.
// */
//public class GmailApiService {
//    private String userEmail = UserFactory.gmailUser().getEmail();
//    private List<String> labelsId = Arrays.asList("INBOX", "SPAM");
//
//    public GmailApiService(){
//
//    }
//
//    public String getBulkId(Gmail service){
//        List<Message> messages = null;
//        try {
//            messages = MessagesList.listMessagesWithLabels(service, userEmail, labelsId);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        for(Message mes: messages){
//            try {
//                System.out.println(mes.toPrettyString());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            System.out.println(mes.toString() + " ---");
//        }
//        return "Bulk_ID";
//    }
//
//
//    private String getAccessToken()
//    {
//        try
//        {
//            Map<String,Object> params = new LinkedHashMap<>();
//            params.put("grant_type","refresh_token");
//            params.put("client_id", "1071174787113-kilmsiskuueu7tj4vfi2hje0qj4jqpnh.apps.googleusercontent.com");
//            params.put("client_secret", "_WgYAVY4D41UCe-9oCHlZ9Ry");
//            params.put("refresh_token",[YOUR REFRESH TOKEN]);
//
//            StringBuilder postData = new StringBuilder();
//            for(Map.Entry<String,Object> param : params.entrySet())
//            {
//                if(postData.length() != 0)
//                {
//                    postData.append('&');
//                }
//                postData.append(URLEncoder.encode(param.getKey(),"UTF-8"));
//                postData.append('=');
//                postData.append(URLEncoder.encode(String.valueOf(param.getValue()),"UTF-8"));
//            }
//            byte[] postDataBytes = postData.toString().getBytes("UTF-8");
//
//            URL url = new URL("https://accounts.google.com/o/oauth2/token");
//            HttpURLConnection con = (HttpURLConnection)url.openConnection();
//            con.setDoOutput(true);
//            con.setUseCaches(false);
//            con.setRequestMethod("POST");
//            con.getOutputStream().write(postDataBytes);
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            StringBuffer buffer = new StringBuffer();
//            for (String line = reader.readLine(); line != null; line = reader.readLine())
//            {
//                buffer.append(line);
//            }
//
//            JsonObject jobj = new Gson().fromJson(buffer.toString(), JsonObject.class);
//            String result = jobj.get("access_token").getAsString();
//            return result;
//        }
//        catch (Exception ex)
//        {
//            ex.printStackTrace();
//        }
//        return null;
//    }
//
//
//}
