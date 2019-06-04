package com.veloeye.Singleton;

import java.util.ArrayList;

public class Singleton {

    private static Singleton single_instance = null;

    public static Singleton getInstance()
    {
        if (single_instance == null)
            single_instance = new Singleton();

        return single_instance;
    }

    public static Singleton getSingle_instance() {
        return single_instance;
    }

    public static void setSingle_instance(Singleton single_instance) {
        Singleton.single_instance = single_instance;
    }

    public String getIsloginFrom() {
        return isloginFrom;
    }

    public void setIsloginFrom(String isloginFrom) {
        this.isloginFrom = isloginFrom;
    }

    public String isloginFrom;

    public String getBikeimages() {
        return bikeimages;
    }

    public void setBikeimages(String bikeimages) {
        this.bikeimages = bikeimages;
    }

    public String bikeimages;

}

