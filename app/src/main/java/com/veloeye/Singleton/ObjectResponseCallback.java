package com.veloeye.Singleton;

import com.google.gson.JsonObject;

public interface ObjectResponseCallback {
    void onSuccess(JsonObject jsonObject);
    void onFailure(String error);
}
