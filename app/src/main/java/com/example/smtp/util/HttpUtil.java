package com.example.smtp.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
    public static String baseUrl = "http://119.39.26.219:8096/";
    static OkHttpClient client = new OkHttpClient();
    public static final MediaType FORM_CONTENT_TYPE
            = MediaType.parse("application/json;charset=utf-8");
    static MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    public static String doGet(String router) {
        String answer = "";
        String url = baseUrl + router;
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            answer = response.body().string();
//            System.out.println(response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer;
    }

    public synchronized  static String doPost(String router, Map<String,String>map) throws JSONException {
        String answer = "";
        String url = baseUrl + router;
        JSONObject json = new JSONObject();
        for (String key : map.keySet()) {
            //追加表单信息
            json.put(key, Objects.requireNonNull(map.get(key)));
        }
        RequestBody requestBody = RequestBody.create(JSON, String.valueOf(json));

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try (Response response = client.newCall(request).execute()) {
            answer = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return answer;

    }
}
