package com.veloeye.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.parse.ParseInstallation;
import com.veloeye.R;
import com.veloeye.Singleton.Singleton;
import com.veloeye.model.data.LoginResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.veloeye.activity.CreateAccountActivity.isregistered;


/**
 * Created by Florin on 11.09.2015.
 */
public class LoginActivity extends AppActivity {
    public static boolean isUserForgetpassword = false;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;
    @BindView(R.id.textview_forgot_password)
    TextView textviewForgotPassword;
    @BindView(R.id.textview_police)
    TextView textViewViewPolice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        settingLoginData();
      /*  etEmail.setText("ammu@ammu.com");
        etPassword.setText("345");*/
    }

    private void settingLoginData() {
        Bundle extras = getIntent().getExtras();


        if (extras != null) {
            String signout = extras.getString("SIGNOUT");
            if (signout.equals("1")) {
                etEmail.setText("");
                etPassword.setText("");

            }
        } else {
            if (isregistered == true) {
                etEmail.setText(prefs.getString("useremail", ""));
                etPassword.setText(prefs.getString("userpassword", ""));
            }
            isregistered = false;
        }

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogIn:
                btnLoginClick();
                break;

            case R.id.btnRegister:
                startActivityForResult(new Intent(this, CreateAccountActivity.class), 1);
                break;

        }
    }

    private void btnLoginClick() {
        if (!validateFields()) return;

        final MaterialDialog progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.please_wait)
                .progress(true, 0)
                .show();
        progressDialog.setCancelable(false);

        veloeyeAPI.login(
                etEmail.getText().toString(),
                etPassword.getText().toString(),
                new Callback<List<LoginResponse>>() {
                    @Override
                    public void success(List<LoginResponse> loginResponses, Response response) {
                        progressDialog.dismiss();
                        Log.d("LoginResponse", loginResponses + "");
                        if (loginResponses.get(0).getUserid().equals("0")) {
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
                                    .putString("email", email)
                                    .putString("userpassword", etPassword.getText().toString())
                                    .putString("useremail", email)
                                    .putString("password", etPassword.getText().toString())
                                    .putString("gender", loginResponse.getGender())
                                    .putString("mobile", loginResponse.getMobile())
                                    .putString("auth", loginResponse.getAuth())
                                    .putString("auth", loginResponse.getAuth())
                                    .putString("signupdate", loginResponse.getSignupdate())
                                    .putString("lastlogindate", loginResponse.getLastlogin())
                                    .apply();
                            Singleton.getInstance().setIsloginFrom("user");
                            // Store email on Parse.com
                            ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                            installation.put("email", email);
                            installation.saveEventually();
                            startActivity(MainActivity.class);
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


    private boolean validateFields() {
        if (etEmail.getText().toString().trim().isEmpty()) return false;
        else if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches())
            return false;

        if (etPassword.getText().toString().trim().isEmpty()) return false;
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            etEmail.setText(prefs.getString("email", ""));
            etPassword.setText(prefs.getString("password", ""));
        }
    }


    @OnClick({R.id.ll_login, R.id.textview_forgot_password, R.id.textview_police})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textview_forgot_password:
                isUserForgetpassword = true;
                startActivity(ForgetPassword.class);
                break;
            case R.id.ll_login:
                startActivity(PoliceLoginActivity.class);
                break;
            case R.id.textview_police:
                startActivity(PoliceLoginActivity.class);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //preventing default implementation previous to android.os.Build.VERSION_CODES.ECLAIR
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

    }
    @Override
    protected  void onResume(){
        super.onResume();
         if(Singleton.getInstance().getIsloginFrom()!=null){
             if(Singleton.getInstance().getIsloginFrom().equals("user")){
                 etEmail.setText(prefs.getString("useremail", ""));
                 etPassword.setText(prefs.getString("userpassword", ""));
             }
         }


    }
}
