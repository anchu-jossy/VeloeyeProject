package com.veloeye.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.veloeye.R;
import com.veloeye.model.data.AddBikeResponse;
import com.veloeye.model.data.Bike;
import com.veloeye.model.data.DeleteResponse;
import com.veloeye.model.data.Image;
import com.veloeye.model.data.Manufacturer;

import java.io.File;
import java.util.ArrayList;
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
public class REgistrationActivity extends AppActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int SELECT_FILE = 1;
    private static final int TAKE_PHOTO = 2;
    File photoFile = null;
    @BindView(R.id.etMake)
    TextView etMake;
    @BindView(R.id.etModel)
    EditText etModel;
    @BindView(R.id.etColor)
    EditText etColor;
    @BindView(R.id.etInfo)
    EditText etInfo;
    @BindView(R.id.etFramenumber)
    EditText etFramenumber;
    String tempImagePath;
    List<Manufacturer> manufacturersList;
    CharSequence[] manCharSequenceList;
    int selectedManufacturer = -1;
    String qrcode;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.textView9)
    ImageView textView9;
    @BindView(R.id.textView_please_enter_police)
    TextView textViewPleaseEnterPolice;
    @BindView(R.id.textView11)
    TextView textView11;
    @BindView(R.id.ll_addbike)
    RelativeLayout llAddbike;
    @BindView(R.id.ll_parent)
    RelativeLayout llParent;
    Bike bike;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    int count = 0;
    int manID = 0;
    @BindView(R.id.btn_register)
    FrameLayout btnRegister;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.dashboard_id)
    LinearLayout dashboardId;
    @BindView(R.id.ll_fields)
    LinearLayout llFields;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_drawer);
        ButterKnife.bind(this);

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        settingUserName();
        getIntentData();

        navView.setNavigationItemSelectedListener(this);
        navView.setItemIconTintList(null);
        getManufacturersList();
        requestPermissions();

        settingDrawerLayout();


    }

    private void getIntentData() {
        if (getIntent().getParcelableExtra("bike") != null) {

            qrcode = getIntent().getStringExtra("qrcode");
            bike = (Bike) getIntent().getParcelableExtra("bike");
            etMake.setText(bike.getMakename());
            etModel.setText(bike.getModel());
            etColor.setText(bike.getColor());
            etFramenumber.setText(bike.getFramecode());


        }
    }

    private void settingUserName() {
        SharedPreferences prefs = getSharedPreferences("veloeye", MODE_PRIVATE);
        tvUsername.setText("Username : " + prefs.getString("username", null));
    }

    private void settingDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


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
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(" https://www.veloeye.com"));
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

    private void getPositionList() {

        for (Manufacturer manufacturer : manufacturersList) {
            if (etMake.getText().equals(manufacturer.getManufacturer())) {
                manID = Integer.parseInt(manufacturer.getManid());

                return;
            }
            count++;

        }
        return;
    }

    private void getManufacturersList() {
        final MaterialDialog progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.please_wait)
                .progress(true, 0)
                .show();
        progressDialog.setCancelable(false);

        veloeyeAPI.getManufacturers(new Callback<List<Manufacturer>>() {
            @Override
            public void success(List<Manufacturer> manufacturers, Response response) {
                progressDialog.dismiss();

                if (manufacturers == null) {
                    return;
                } else {
                    manufacturersList = manufacturers;

                    // create array of manufacturers for list adapter
                    manCharSequenceList = new CharSequence[manufacturersList.size()];
                    int i = 0;
                    for (Manufacturer manufacturer : manufacturersList) {
                        manCharSequenceList[i++] = manufacturer.getManufacturer();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                showErrorDialog(R.string.error, R.string.server_not_responding);
                Log.e("GetManufacturers", error.getMessage());
            }
        });
    }


    public void callAPI(String qrcode) {
        final MaterialDialog progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.please_wait)
                .progress(true, 0)
                .show();
        progressDialog.setCancelable(false);

        if (manufacturersList.get(selectedManufacturer).getManid().toString().equals("") || etModel.getText().toString().equals("") ||
                etColor.getText().toString().equals("") || etInfo.getText().toString().equals("") || etFramenumber.getText().toString().equals("")) {
            Toast.makeText(this, "Please Enter All fields", Toast.LENGTH_LONG);
        } else {
            veloeyeAPI.addBike(
                    qrcode,
                    prefs.getString("userid", ""),
                    manufacturersList.get(selectedManufacturer).getManid(),
                    etModel.getText().toString(),
                    etColor.getText().toString(),
                    etInfo.getText().toString(),
                    etFramenumber.getText().toString(),
                    new Callback<List<AddBikeResponse>>() {
                        @Override
                        public void success(List<AddBikeResponse> addBikeResponses, Response response) {
                            if (addBikeResponses == null) {
                                progressDialog.dismiss();
                                showErrorDialog(R.string.error, R.string.check_edit_fields);
                            } else {
                                progressDialog.dismiss();
                                Intent mainIntent = new Intent(getApplicationContext(), ImageUploading.class);
                                //  Bundle dataBundle = new Bundle();
                                ArrayList<AddBikeResponse> addBikeResponseArrayList = new ArrayList<>();
                                addBikeResponseArrayList.add(addBikeResponses.get(0));
                                mainIntent.putParcelableArrayListExtra("key", (ArrayList<? extends Parcelable>) addBikeResponseArrayList);
                                mainIntent.putExtra("QRCODE", qrcode);
                                startActivity(mainIntent);


                            }

                        }

                        @Override
                        public void failure(RetrofitError error) {
                            progressDialog.dismiss();
                            showErrorDialog(R.string.error, R.string.server_not_responding);
                            Log.e("AddBike", error.getMessage());
                        }
                    });
        }

    }


    private void getEditBike(Bike bike) {
        final MaterialDialog progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.please_wait)
                .progress(true, 0)
                .show();
        progressDialog.setCancelable(false);
        veloeyeAPI.deleteBike(bike.getQrcode(), bike.getBikeid(), bike.getUserid(), manID, etModel.getText().toString(), etColor.getText().toString(), "2019", bike.getFramecode(), "2", new Callback<List<DeleteResponse>>() {


            @Override
            public void success(List<DeleteResponse> deleteResponses, Response response) {
                progressDialog.dismiss();
                if (deleteResponses.get(0).getResponse().equals("1")) {
                    //  Toast.makeText(REgistrationActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
                    Intent editIntent = new Intent(getApplicationContext(), ImageUploading.class);
                    editIntent.putExtra("bike", bike);

                    startActivity(editIntent);
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

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etMake:
                new MaterialDialog.Builder(this)
                        .title(R.string.app_name)
                        .items(manCharSequenceList)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                selectedManufacturer = which;
                                etMake.setText(text);
                            }
                        })
                        .show();
                break;


        }
    }


    private boolean validateFields() {
        if (!checkEmptyEditText(etModel)) return false;
        if (!checkEmptyEditText(etColor)) return false;
        if (!checkEmptyEditText(etInfo)) return false;
        if (!checkEmptyEditText(etFramenumber)) return false;

        if (selectedManufacturer == -1) {
            Toast.makeText(this, R.string.select_manufacturer, Toast.LENGTH_LONG).show();
            return false;
        }

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


    private void requestPermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE

                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (!report.areAllPermissionsGranted()) {
                    Toast.makeText(REgistrationActivity.this, R.string.perm_camera_storage, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }

        }).check();


    }


    @OnClick({R.id.textView9, R.id.btn_register, R.id.dashboard_id})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textView9:
                break;
            case R.id.btn_register:
                getPositionList();
                if (getIntent().getParcelableExtra("bike") != null) {
                    if (!bike.getQrcode().equals("")) {

                        getEditBike(bike);
                        return;
                    }
                }
                if (validateFields())
                    callAPI(qrcode);
                break;
            case R.id.dashboard_id:
                startActivity(MainActivity.class);
        }


    }


    @OnClick(R.id.dashboard_id)
    public void onViewClicked() {
    }
}
