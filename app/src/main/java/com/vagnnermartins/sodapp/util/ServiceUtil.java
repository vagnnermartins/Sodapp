package com.vagnnermartins.sodapp.util;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class ServiceUtil {

    public static HttpResponse executarGet(String path) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(path);
        httpGet.addHeader("Content-Type", "application/json;");
        httpGet.setParams(HTTPUtil.setTimeout());
        return httpclient.execute(httpGet);
    }

}
