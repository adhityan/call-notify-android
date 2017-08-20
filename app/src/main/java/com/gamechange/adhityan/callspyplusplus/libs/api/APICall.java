package com.gamechange.adhityan.callspyplusplus.libs.api;

import android.net.Uri;
import android.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import com.gamechange.adhityan.callspyplusplus.libs.Utilities;

public class APICall {
    private static boolean hasInit = false;
    private static final String apiKey = "1c1e1fskda6a3c0kskdfe714d773baf5x";

    public static void init() {
        if (hasInit) return; hasInit = true;
    }

    public APICall(apiInterface a, String url, String code) {
        this(a, url, code, null);
    }
    public APICall(apiInterface a, String url, String code, List<Pair<String, String>> get) {
        this(a, url, code, get, null);
    }

    public APICall(apiInterface a, String url, String code, List<Pair<String, String>> get, List<Pair<String, String>> post) {
        Utilities.logDebug("Making HTTP Call: " + url);
        APIResponse r = call(url, get, post);

        if (r.status == 1) a.apiResponse(r.result, code);
        else if (r.status == 0) a.apiError(r.result, code);
        else {
            Utilities.logError("CALL FAILED WITH CODE " + r.status + ": " + url);
            a.connectionError(r.status, code);
        }
    }

    public static APIResponse call(String url) {
        return call(url, null, null);
    }
    public static APIResponse call(String url, List<Pair<String, String>> get) {
        return call(url, get, null);
    }

    public static APIResponse call(String url, List<Pair<String, String>> get, List<Pair<String, String>> post) {
        if (get != null) {
            Uri.Builder ub = Uri.parse(url).buildUpon();

            for (Pair<String, String> entry : get) {
                ub.appendQueryParameter(entry.first, entry.second);
            }

            url = ub.build().toString();
        }

        Boolean isPost = false;
        Request request = new Request.Builder()
                .addHeader("X-HTTP-Y-API-KEY", apiKey)
                .cacheControl(CacheControl.FORCE_NETWORK)
                .url(url)
                .build();

        if (post != null) {
            isPost = true;
            FormBody.Builder postBuilder = new FormBody.Builder();
            for (Pair<String, String> entry : post) {
                if (entry.first != null && entry.second != null) {
                    postBuilder.add(entry.first, entry.second);
                }
            }
            RequestBody postBuild = postBuilder.build();

            request = new Request.Builder()
                    .addHeader("X-HTTP-Y-API-KEY", apiKey)
                    .cacheControl(CacheControl.FORCE_NETWORK)
                    .url(url)
                    .post(postBuild)
                    .build();
        }

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS);
        OkHttpClient client = clientBuilder.build();

        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();

            String type = "GET";
            if (isPost) type = "POST";
            Utilities.logDebug(type + ": " + url + " Response code: " + response.code());

            int status = 1;
            if (response.header("Error", null) != null) status = 0;
            else if (result.equals("")) status = 0;
            return new APIResponse(result, status);
        }
        catch (IOException e) {
            return new APIResponse("", 3);
        }
    }
}