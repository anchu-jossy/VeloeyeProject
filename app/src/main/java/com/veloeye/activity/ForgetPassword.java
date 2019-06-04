package com.veloeye.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.veloeye.R;
import com.veloeye.model.data.ForgetPasswordResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.veloeye.activity.LoginActivity.isUserForgetpassword;

public class ForgetPassword extends AppActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.btn_reset_password)
    FrameLayout btnReset;
    @BindView(R.id.textview_account_created)
    TextView textviewAccountCreated;
    @BindView(R.id.textview_account_created_profile)
    TextView textviewAccountCreatedProfile;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.textView11)
    TextView textView11;
    @BindView(R.id.linearLayout)
    LinearLayout linearLayout;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);
        context = this;


    }

    private void callForgetPasswordApi(String emailAddress) {
        final MaterialDialog progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.please_wait)
                .progress(true, 0)
                .show();
        progressDialog.setCancelable(false);

        veloeyeAPI.apiCallForForgetpassword(emailAddress, new Callback<List<ForgetPasswordResponse>>() {
            @Override
            public void success(List<ForgetPasswordResponse> forgetPasswordResponses, Response response) {
                progressDialog.dismiss();

                Log.e("forgetpassword", forgetPasswordResponses.get(0).getResponse().toString());

                if (forgetPasswordResponses.get(0).getResponse().toString().equals("1")) {
                    showAlert();

                }
            }


            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                Log.e("forgetpassword", String.valueOf(error));
            }
        });
    }

    private void showAlert() {
        new AlertDialog.Builder(ForgetPassword.this)
                .setTitle("Please Check your Email")

                .setCancelable(true)
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (isUserForgetpassword == true) {
                            startActivity(LoginActivity.class);
                            isUserForgetpassword = false;
                        } else {
                            startActivity(PoliceLoginActivity.class);
                        }
                    }
                })
                .show();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

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
        }else if (id == R.id.nav_signout) {
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


    @OnClick(R.id.btn_reset_password)
    public void onViewClicked() {
        String emailAddress = etEmail.getText().toString();
        if (emailAddress != null) {
            callForgetPasswordApi(emailAddress);

        }


    }


}
