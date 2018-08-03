package com.ambit.otgorithm.models;

import com.ambit.otgorithm.remote.APIService;
import com.ambit.otgorithm.remote.RetrofitClient;

public class Common {
    public static String currentToken = "";

    private static String baseUrl = "https://fcm.googleapis.com/";

    public static APIService getFCMClient(){
        return RetrofitClient.getClient(baseUrl).create(APIService.class);
    }
}
