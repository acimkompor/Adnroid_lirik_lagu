package com.acimsoft.lha.lirik_project.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroClient {
    private static final String URL ="https://api.keysya.com/lirik/";
    private static Retrofit retrofit;

    private static Retrofit getClient() {
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiRetro getApiRetro() {
        return getClient().create(ApiRetro.class);
    }
}
