package com.veloeye.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.veloeye.Singleton.ArrayResponseCallback;
import com.veloeye.R;
import com.veloeye.Singleton.Singleton;
import com.veloeye.api.Connection;
import com.veloeye.model.data.Constants;
import com.veloeye.model.data.Enquiry;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


import static com.veloeye.activity.PoliceLoginActivity.policelogin;


public class ContactInfoActivity extends AppActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    SharedPreferences prefs;
    String userid = "";
    EditText subjectEditText;
    EditText enquiryEditText;
    FrameLayout btnSubmit;
    TextView tvUsername;
    List<Enquiry> responseEnquiry = new ArrayList<>();
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    Enquiry enquiry;
    @BindView(R.id.dashboard_id)
    LinearLayout dashboardId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactinfo_drawer);
        ButterKnife.bind(this);

        //  prefs = getSharedPreferences("veloeye", MODE_PRIVATE);
        RelativeLayout relativeLayout = findViewById(R.id.linear_layout_parent);
        subjectEditText = findViewById(R.id.subjectText);
        enquiryEditText = findViewById(R.id.enquiryText);
        tvUsername = findViewById(R.id.tv_username);
        btnSubmit = findViewById(R.id.btnSubmit);
        enquiry = new Enquiry();
        settingUserName();
        settingDrawerLayout();

    }

    private void settingUserName() {
        SharedPreferences prefs = getSharedPreferences("veloeye", MODE_PRIVATE);
        userid = prefs.getString("userid", "");
        tvUsername.setText("Loggedin as : " + prefs.getString("username", null));


    }

    private void settingDrawerLayout() {
        navView.setNavigationItemSelectedListener(this);
        navView.setItemIconTintList(null);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (!Singleton.getInstance().getIsloginFrom().isEmpty()) {

            if (Singleton.getInstance().getIsloginFrom().equals("police")) {

                navView.inflateMenu(R.menu.police_menu);

            } else if (Singleton.getInstance().getIsloginFrom().equals("user")) {
                navView.inflateMenu(R.menu.menu);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_profile) {
            startActivity(ProfileActivity.class);
            // Handle the camera action
        } else if (id == R.id.nav_mybikes) {
            startActivity(BikesActivity.class);
        } else if (id == R.id.nav_scan) {
            startActivity(ScanActivity.class);
        } else if (id == R.id.nav_facebook) {
            Intent fbIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/veloeyeapp/"));
            startActivity(fbIntent);

        } else if (id == R.id.nav_youtube) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCw6KqEhyydCQVrA0GSzaoeg")));
        } else if (id == R.id.nav_twitter) {
            Intent twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/veloeye"));
            startActivity(twitterIntent);
        } else if (id == R.id.nav_instagram) {
            Intent instaIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/veloeye/"));
            startActivity(instaIntent);
        } else if (id == R.id.nav_website) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.veloeye.com"));
            startActivity(browserIntent);
        } else if (id == R.id.nav_signout) {
            Intent mainIntent = new Intent(getApplicationContext(), LoginActivity.class);
            Bundle dataBundle = new Bundle();
            dataBundle.putString("SIGNOUT", "1");
            mainIntent.putExtras(dataBundle);
            startActivity(mainIntent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void apiCallForStolenBike(Enquiry enquiry) {

        String subjectText = subjectEditText.getText().toString();
        String enquiryText = enquiryEditText.getText().toString();

        if (subjectText.equals("") || enquiry.equals("")) {
            Toast.makeText(ContactInfoActivity.this, " Please Enter Subject and Enquiry", Toast.LENGTH_SHORT).show();

        } else {
            if (enquiry != null) {
                Connection.with(this, Constants.SUBMIT_ENQUIRY + "userid=" + userid + "&" + "subject=" + subjectText + "&" + "enquiry=" + enquiryText)
                        .setMethod(Connection.Method.GET)
                        .performNetworkCallForArray(new ArrayResponseCallback() {
                            @Override
                            public void onSuccess(JsonArray jsonObject) {
                                if (jsonObject != null) {
                                    if (responseEnquiry != null) {


                                        responseEnquiry.clear();

                                        List<Enquiry> temp = new Gson().fromJson(jsonObject.toString(), new TypeToken<List<Enquiry>>() {
                                        }.getType());
                                        if (temp.get(0).getResponse().equals("1")) {
                                            responseEnquiry.addAll(temp);
                                            Toast.makeText(ContactInfoActivity.this, "Enquiry Successfully Submit", Toast.LENGTH_SHORT).show();
                                            startActivity(MainActivity.class);

                                        } else {
                                            Toast.makeText(ContactInfoActivity.this, "Network Error Please try Again", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(String error) {

                            }
                        }, true);
            }

        }
    }


    @OnClick({R.id.dashboard_id, R.id.btnSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                apiCallForStolenBike(enquiry);
                break;
            case R.id.dashboard_id:
                Intent intent = new Intent(ContactInfoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}
