package com.mecodroid.blood_bank.data.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// public class to build Retrofit with url with GsonConverterFactory converter
public class RetrfitClient {

    private static final String BASE_API_URL = "http://ipda3-tech.com/blood-bank/api/v1/";
    public static Retrofit retrofit = null;

    public static Retrofit getClient() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_API_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;

    }
}

