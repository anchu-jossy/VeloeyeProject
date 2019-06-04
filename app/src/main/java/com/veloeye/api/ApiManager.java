package com.veloeye.api;

import android.util.Log;

import retrofit.RestAdapter;


/**
 * Created by Florin Mihalache on 14.09.2015.
 */

public class ApiManager {
    public static final String API_BASE_URL = "https://appservices.veloeye.com";
    private static VeloeyeAPI apiInstance;

    public static VeloeyeAPI getApiInstance() {


        if (apiInstance == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(API_BASE_URL)

                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            apiInstance = restAdapter.create(VeloeyeAPI.class);
          /*  Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            apiInstance= retrofit.create(VeloeyeAPI.class);*/
        }


        return apiInstance;


    }


}
