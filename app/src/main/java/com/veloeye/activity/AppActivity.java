package com.veloeye.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.veloeye.api.ApiManager;
import com.veloeye.api.VeloeyeAPI;


public abstract class AppActivity extends AppCompatActivity {
    VeloeyeAPI veloeyeAPI;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        veloeyeAPI = ApiManager.getApiInstance();
        prefs = getSharedPreferences("veloeye", MODE_PRIVATE);
    }

    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    protected void showErrorDialog(int resIdTitle, int resIdContent, Theme theme) {
        new MaterialDialog.Builder(this)
                .title(resIdTitle)
                .content(resIdContent)
                .neutralText("OK")
                .theme(theme)
                .show();
    }

    protected void showErrorDialog(int resIdTitle, int resIdContent) {
        new MaterialDialog.Builder(this)
                .title(resIdTitle)
                .content(resIdContent)
                .neutralText("OK")
                .show();
    }
}
