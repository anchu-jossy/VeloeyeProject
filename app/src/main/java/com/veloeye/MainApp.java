package com.veloeye;

import android.app.Application;
import android.os.StrictMode;

import com.karumi.dexter.Dexter;
import com.parse.Parse;
import com.parse.ParseInstallation;


/**
 * Created by Florin on 08.01.2016.
 */
public class MainApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new  Parse.Configuration.Builder(this)
                .server("https://appservices.veloeye.com").build());

        ParseInstallation.getCurrentInstallation().saveInBackground();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


    }
}
