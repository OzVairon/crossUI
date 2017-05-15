package com.idp.engine.net;

import com.badlogic.gdx.Net;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.centergame.starttrack.api.StartTrackApi;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by ozvairon on 12.05.17.
 */

public class Api {

    protected static final int REQUEST_TIMEOUT_SEC = 30;

    private static Gson GSON; // !! never use directly!

    private static String API_URL  = "http://st.center-game.com";
    //private static String API_URL  = "http://dhabensky.pythonanywhere.com";

    protected static String privateToken;

    protected static Gson getGson() {
        if (GSON == null) {
            // !! never put this in static contructor!
            GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        }
        return GSON;
    }
    protected static IdpRequest request(
            String method,
            String apiCall,
            Object params,
            Net.HttpResponseListener listener) {

        String s = "";
//        if (method.equals("GET")) {
//            if (params != null && !params.isEmpty())
//                s = "?" + HttpParametersUtils.convertHttpParameters(params);
//        }
        String url = API_URL + "/" + apiCall + s;
        System.out.println(method + " " + url);

        IdpRequest req = new IdpRequest(url);
        HttpRequestBuilder builder = req.getRequestBuilder();
        builder.method(method).timeout(REQUEST_TIMEOUT_SEC * 1000);
        builder.header("Content-Type",   "application/json");
        builder.header("Accept",         "application/json");
        if (privateToken != null)
            builder.header("Authorization",  "Token " + privateToken);
        if (method.equals("POST")) {
            if (params != null) {
                builder.content(getGson().toJson(params));
                System.out.println(getGson().toJson(params));
            }
        }
        req.getRequestBuilder().followRedirects(false);
        req.listener(listener).load();
        return req;
    }

    public static void setApiUrl(String apiUrl) {
        API_URL = apiUrl;
    }

    /**
     * @return private token currently used in all requests that require authentication
     */
    public static String getPrivateToken() {
        return privateToken;
    }

    /**
     * Sets private token that will be used in all requests that require authentication.
     */
    public static void setPrivateToken(String privateToken) {
        StartTrackApi.privateToken = privateToken;
    }
}
