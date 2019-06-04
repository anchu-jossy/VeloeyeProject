package com.veloeye.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.veloeye.R;
import com.veloeye.model.data.TransferBikeToNEWResponse;
import com.veloeye.model.data.TransferCheckResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class TransferBikeActivity extends AppActivity {


    String bikeid;
    String username;
    @BindView(R.id.imgView_police)
    ImageView imgViewPolice;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.etUsername)
    EditText etUsername;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.btnTransfer)
    FrameLayout btnTransfer;
    @BindView(R.id.linearLayout2)
    LinearLayout linearLayout2;
    @BindView(R.id.rl_parent_login)
    RelativeLayout rlParentLogin;
    FrameLayout btnConfirm;
    TextView textViewTranscode;
    TransferCheckResponse transferCheckResponse;
    Dialog dialog;

    @SuppressLint("ResourceType")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trasfer_bike);
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            bikeid = extras.getString("BIKEID");


        }


    }

    private void getTransfer(String username) {
        final MaterialDialog progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.please_wait)
                .progress(true, 0)
                .show();
        progressDialog.setCancelable(false);
        veloeyeAPI.transferBike(username, bikeid, new Callback<List<TransferCheckResponse>>() {

            @Override
            public void success(List<TransferCheckResponse> transferCheckResponseslist, Response response) {

                progressDialog.dismiss();
               if(response.toString().equals("1"))
               {
                   transferCheckResponse = transferCheckResponseslist.get(0);
                   getTransferCodeAlert();
               }

            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                showErrorDialog(R.string.error, R.string.server_not_responding);
                Log.e("Bikes", error.getMessage());
            }
        });
    }

    private void getTransferToNew( String transfercode) {

        final MaterialDialog progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.please_wait)
                .progress(true, 0)
                .show();
        progressDialog.setCancelable(false);
        veloeyeAPI.transferBikeToNEW(transferCheckResponse.getUserid(), transfercode, new Callback<List<TransferBikeToNEWResponse>>() {

            @Override
            public void success(List<TransferBikeToNEWResponse> transferBikeToNEWResponse, Response response) {

                progressDialog.dismiss();
                dialog.dismiss();
                if(response.toString().equals("1")) {
                    if (!transferBikeToNEWResponse.get(0).getResponse().toString().equals(null))
                        if (transferBikeToNEWResponse.get(0).getResponse().toString().equals("1")) {
                            Toast.makeText(TransferBikeActivity.this, "Successfully transfered", Toast.LENGTH_SHORT).show();
                            startActivity(MainActivity.class);
                        }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                showErrorDialog(R.string.error, R.string.server_not_responding);
                Log.e("Bikes", error.getMessage());
            }
        });
    }


    private void getTransferCodeAlert() {
        dialog = new Dialog(TransferBikeActivity.this);
        dialog.setContentView(R.layout.transfercode);
        btnConfirm = dialog.findViewById(R.id.btn_confirm);
        textViewTranscode = dialog.findViewById(R.id.textView_transfer);

        textViewTranscode.setText(transferCheckResponse.getTxcode());
        dialog.show();
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getTransferToNew( transferCheckResponse.getTxcode());
            }
        });
        dialog.setCancelable(true);
    }

    @OnClick(R.id.btnTransfer)
    public void onViewClicked() {
        username = etUsername.getText().toString();

        getTransfer(username.trim());
    }
}