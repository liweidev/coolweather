package com.example.coolweather.utils;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by liwei on 2017/2/21.
 */

public class HttpUtils {

    /**
     * 发送Http网络请求--GET
     * @param url
     * @param callback
     */
    public static void sendHttpRequest(String url, Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder()
                .url(url)
                .method("GET",null)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
