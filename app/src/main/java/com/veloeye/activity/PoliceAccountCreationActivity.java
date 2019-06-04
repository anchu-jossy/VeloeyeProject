package com.veloeye.activity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.veloeye.R;
import com.veloeye.model.data.RegisterResponse;

import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Florin on 11.09.2015.
 */
public class PoliceAccountCreationActivity extends AppActivity {
    @BindView(R.id.etFirstName)
    EditText etFirstName;
    @BindView(R.id.etLastName)
    EditText etLastName;
    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPhone)
    EditText etPhone;
    @BindView(R.id.btn_create_account)
    FrameLayout btnCreateAccount;

    Boolean isEmailValid = false;
    static  Boolean isregisteredpolice=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

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

    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_create_account:
                if (!validateFields()) return;
                isEmailValid = checkEmail(etEmail.getText().toString());
                final MaterialDialog progressDialog = new MaterialDialog.Builder(this)
                        .content(R.string.please_wait)
                        .progress(true, 0)
                        .show();
                progressDialog.setCancelable(false);
/// gender is hardcorded for new ui
                Bundle extras = getIntent().getExtras();
                if (extras != null) {
                    String policelogin = extras.getString("POLICELOGIN");
                    if (policelogin.equals("1")) {
                        if (isEmailValid == true) {
                            veloeyeAPI.createAccountForPolice(
                                    etFirstName.getText().toString(),
                                    etLastName.getText().toString(),
                                    etEmail.getText().toString(),
                                    etPhone.getText().toString(),
                                    etPassword.getText().toString(),
                                    new Callback<List<RegisterResponse>>() {
                                        @Override
                                        public void success(List<RegisterResponse> registerResponses, Response response) {
                                            progressDialog.dismiss();
                                            Log.d("policeaccount1111", registerResponses.get(0).getUsername() + "");
                                            if (registerResponses.get(0).getResponse() == 0) {
                                                showErrorDialog(R.string.error, R.string.sending_data_wrong);
                                            } else {
                                               isregisteredpolice=true;
                                                prefs.edit()
                                                        .putString("email", etEmail.getText().toString())
                                                        .putString("password", etPassword.getText().toString())
                                                        .apply();
                                                Toast.makeText(PoliceAccountCreationActivity.this,
                                                        R.string.account_created, Toast.LENGTH_LONG).show();
                                                startActivity(PoliceLoginActivity.class);

                                            }
                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            progressDialog.dismiss();
                                            showErrorDialog(R.string.error, R.string.server_not_responding);
                                            Log.e("Login", error.getMessage());
                                        }
                                    });
                        }
                    }
                    isEmailValid = false;
                }
                break;
        }
    }

    private boolean validateFields() {
        if (!checkEmptyEditText(etFirstName)) return false;
        if (!checkEmptyEditText(etLastName)) return false;
        if (!checkEmptyEditText(etPassword)) return false;
        if (!checkEmptyEditText(etEmail)) return false;
        if (!checkEmptyEditText(etPhone)) return false;


        if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) return false;


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

/*    private String getGender() {
        int selId = rgGender.getCheckedRadioButtonId();
        if (selId == R.id.rbMale) {
            return "1";
        } else
            return "2";
    }*/
}
