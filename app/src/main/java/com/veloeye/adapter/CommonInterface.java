package com.veloeye.adapter;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.veloeye.model.data.Bike;

public interface CommonInterface {
    default void delete(Bike bike) {

    }
}
