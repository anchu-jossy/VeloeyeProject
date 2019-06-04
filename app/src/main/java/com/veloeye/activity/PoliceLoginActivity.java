package com.veloeye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.veloeye.R;
import com.veloeye.Singleton.Singleton;
import com.veloeye.model.data.LoginResponse;

import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.veloeye.activity.PoliceAccountCreationActivity.isregisteredpolice;

public class PoliceLoginActivity extends AppActivity {

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z\\+\\.\\_\\%\\-\\+]{4,64}" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    "\\." +
                    "pnn" +
                    "\\." +
                    "police" +
                    "\\." +
                    "uk"
    );
    static String policelogin;
    @BindView(R.id.btnLogIn)
    FrameLayout btnLogIn;
    @BindView(R.id.btnRegister)
    FrameLayout btnRegister;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.textview_forgot_password)
    TextView textviewForgotPassword;
    boolean flag;
    MaterialDialog progressDialog;
    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.police_login);
        ButterKnife.bind(this);
        extras = getIntent().getExtras();

        if (extras != null) {
            String signout = extras.getString("SIGNOUT");
            Log.d("onsignout", "0");
            if (signout.equals("1")) {
                etEmail.setText("");
                etPassword.setText("");

            }
        } else {
            if (isregisteredpolice == true) {
                Log.d("onsignout", "0");
               /* etEmail.setText(prefs.getString("policeemail", ""));
                etPassword.setText(prefs.getString("policepassword", ""));*/
            }

        }


    }

    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (extras != null) {
            String signout = extras.getString("SIGNOUT");
            Log.d("onsignout", "fgfg");
            if (signout.equals("1")) {
                etEmail.setText("");
                etPassword.setText("");

            }
        } else {
            if (Singleton.getInstance().getIsloginFrom()!=null){
                if (Singleton.getInstance().getIsloginFrom().equals("police")) {

                    if (isregisteredpolice == true) {
                        etEmail.setText("");
                        etPassword.setText("");
                        Log.d("onsignout", "gfdg");
                        isregisteredpolice = false;
               /* etEmail.setText(prefs.getString("policeemail", ""));
                etPassword.setText(prefs.getString("policepassword", ""));*/
                    }
                    else{
                        etEmail.setText(prefs.getString("policeemail", ""));
                        etPassword.setText(prefs.getString("policepassword", ""));
                    }

                }
            }

        }

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogIn:
                flag = checkEmail(etEmail.getText().toString());
                String policeEmail = etEmail.getText().toString();
                String policePassword = etPassword.getText().toString();
                if (policeEmail.equals("") || policePassword.equals("")) {
                    Toast.makeText(this, "Please Enter UserName and Password", Toast.LENGTH_SHORT).show();
                } else {
                    if (flag == true) {
                        progressDialog = new MaterialDialog.Builder(this)
                                .content(R.string.please_wait)
                                .progress(true, 0)
                                .show();
                        progressDialog.setCancelable(false);
                        apicallPoliceLogin();
                    }
                    flag = false;
                }
                break;

            case R.id.btnRegister:
                Bundle dataBundle1 = new Bundle();
                Intent mainIntent1 = new Intent(getApplicationContext(), PoliceAccountCreationActivity.class);
                dataBundle1.putString("POLICELOGIN", "1");
                mainIntent1.putExtras(dataBundle1);
                startActivity(mainIntent1);
                finish();

                break;
        }
    }

    private void apicallPoliceLogin() {
        veloeyeAPI.loginForPolice(
                etEmail.getText().toString(),
                etPassword.getText().toString(),
                new Callback<List<LoginResponse>>() {
                    @Override
                    public void success(List<LoginResponse> loginResponses, Response response) {
                        progressDialog.dismiss();


                        if (loginResponses.get(0).getResponse() == 0) {
                            showErrorDialog(R.string.login_failed, R.string.incorrect_email_password);
                        } else {
                            LoginResponse loginResponse = loginResponses.get(0);
                            final String email = etEmail.getText().toString();
                            Log.d("getUsername", loginResponse.getUsername());
                            prefs.edit()
                                    .putString("userid", loginResponse.getUserid())
                                    .putString("username", loginResponse.getUsername())
                                    .putString("fname", loginResponse.getFname())
                                    .putString("sname", loginResponse.getSname())
                                    .putString("policeemail", email)
                                    .putString("policepassword", etPassword.getText().toString())
                                    .putString("email", email)
                                    .putString("password", etPassword.getText().toString())
                                    .putString("gender", loginResponse.getGender())
                                    .putString("mobile", loginResponse.getMobile())
                                    .putString("auth", loginResponse.getAuth())
                                    .apply();

                            Bundle dataBundle = new Bundle();
                            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                            dataBundle.putString("POLICELOGIN", "1");
                            Singleton.getInstance().setIsloginFrom("police");

                            mainIntent.putExtras(dataBundle);
                            startActivity(mainIntent);
                            finish();

                        }
                    }


                    @Override
                    public void failure(RetrofitError error) {
                        progressDialog.dismiss();
                        showErrorDialog(R.string.login_failed, R.string.server_not_responding);
                        Log.e("Login", error.getMessage());
                    }
                });
    }
    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            return false;
        }
        return super.onKeyDown(keyCode, event);
    }*/

    @Override
    public void onBackPressed() {
        if (extras != null) {
            String signout = extras.getString("SIGNOUT");
            if (signout.equals("1")) {


            }
        } else {
            super.onBackPressed();

        }

    }

    @OnClick(R.id.textview_forgot_password)
    public void onViewClicked() {
        startActivity(ForgetPassword.class);
    }
}

