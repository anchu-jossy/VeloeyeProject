package com.veloeye.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.veloeye.R;
import com.veloeye.Singleton.Singleton;
import com.veloeye.model.data.UpdateProfileResponse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Florin on 11.09.2015.
 */
public class ProfileActivity extends AppActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    SharedPreferences prefs;
    @BindView(R.id.dashboard_id)
    LinearLayout dashbord_icon;
    @BindView(R.id.textview_account_created)
    TextView textviewAccountCreated;
    @BindView(R.id.textview_account_created_profile)
    TextView textviewAccountCreatedProfile;
    @BindView(R.id.textview_terms_accepted)
    TextView textviewTermsAccepted;
    @BindView(R.id.textView11)
    TextView textView11;
    @BindView(R.id.btnUpdate)
    FrameLayout btnUpdate;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    Context context;
    Date signupdate, lastlogindate;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        ButterKnife.bind(this);
        context = this;
        prefs = getSharedPreferences("veloeye", MODE_PRIVATE);
        setData();
        settingUserName();
        settingDrawerLayout();
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
    protected void onResume() {
        super.onResume();

        etFirstName.setText(prefs.getString("fname", ""));
        etLastName.setText(prefs.getString("sname", ""));
        etEmail.setText(prefs.getString("email", ""));
        etPhone.setText(prefs.getString("mobile", ""));
        etPassword.setText(prefs.getString("password", ""));
    }

    private void setData() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        SimpleDateFormat formatterOut = new SimpleDateFormat("dd MMM yyyy,hh:mm a");

        try {

            signupdate = formatter.parse(prefs.getString("signupdate", ""));
            textviewAccountCreatedProfile.setText("Account created :" + formatterOut.format(signupdate));
            lastlogindate = formatter.parse(prefs.getString("lastlogindate", ""));
            textviewTermsAccepted.setText("Terms accepted :" + formatterOut.format(lastlogindate));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("fname1", prefs.getString("fname", "") + "" + etFirstName.getText());
        Log.d("fname2", (prefs.getString("sname", "")) + "" + etLastName.getText());
        Log.d("fname3", prefs.getString("email", "") + "" + etEmail.getText());
        Log.d("fname4", prefs.getString("password", "") + "" + etPhone.getText());


    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnUpdate:
                if (!validateFields()) return;

                String password = etPassword.getText().toString();
                if (password.trim().isEmpty()) {
                    password = prefs.getString("password", "");
                }

                final MaterialDialog progressDialog = new MaterialDialog.Builder(this)
                        .content(R.string.please_wait)
                        .progress(true, 0)
                        .show();
                progressDialog.setCancelable(false);
                final String finalPassword = password;
                veloeyeAPI.updateProfile(
                        prefs.getString("userid", ""),
                        etFirstName.getText().toString(),
                        etLastName.getText().toString(),
                        etEmail.getText().toString(),
                        "1",
                        etPhone.getText().toString(),
                        password,
                        new Callback<List<UpdateProfileResponse>>() {
                            @Override
                            public void success(List<UpdateProfileResponse> registerResponses, Response response) {
                                progressDialog.dismiss();

                                if (registerResponses.get(0).getResponse().equals("0")) {
                                    showErrorDialog(R.string.error, R.string.sending_data_wrong);
                                } else {
                                    prefs.edit()
                                            .putString("fname", etFirstName.getText().toString())
                                            .putString("sname", etLastName.getText().toString())
                                            .putString("password", finalPassword)
                                            .putString("gender", etEmail.getText().toString())
                                            .putString("mobile", etPhone.getText().toString())
                                            .apply();
                                    Toast.makeText(ProfileActivity.this, R.string.profile_updated,
                                            Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                progressDialog.dismiss();
                                showErrorDialog(R.string.error, R.string.server_not_responding);
                                Log.e("Login", error.getMessage());
                            }
                        });
                break;
        }
    }

    private void settingUserName() {
        SharedPreferences prefs = getSharedPreferences("veloeye", MODE_PRIVATE);
        tvUsername.setText("Username : " + prefs.getString("username", null));
    }

    private boolean validateFields() {
        if (!checkEmptyEditText(etFirstName)) return false;
        if (!checkEmptyEditText(etLastName)) return false;
        if (!checkEmptyEditText(etPhone)) return false;

        return true;
    }

    private boolean checkEmptyEditText(EditText editText) {
        if (editText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this,
                    editText.getHint() + " " + getString(R.string.field_cant_be_empty),
                    Toast.LENGTH_LONG).show();
            editText.requestFocus();
            return false;
        }
        return true;
    }


    @OnClick(R.id.dashboard_id)
    public void onViewClicked() {
        startActivity(MainActivity.class);
        finish();
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
}
