package com.veloeye.Singleton;

import com.google.gson.JsonArray;

public interface ArrayResponseCallback {
    void onSuccess(JsonArray jsonObject);
    void onFailure(String error);
}
