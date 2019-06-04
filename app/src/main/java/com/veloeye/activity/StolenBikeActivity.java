package com.veloeye.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.veloeye.R;
import com.veloeye.Singleton.Singleton;
import com.veloeye.model.data.StolenBikeLocation;
import com.veloeye.model.data.StolenScansResponse;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.veloeye.activity.PoliceLoginActivity.policelogin;


public class StolenBikeActivity extends AppActivity
        implements NavigationView.OnNavigationItemSelectedListener, LocationListener {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager locationManager;
    @BindView(R.id.btn_report)
    FrameLayout btnReport;
    @BindView(R.id.textview_username)
    TextView textviewUsername;
    @BindView(R.id.textView_contact)
    TextView textViewContact;
    @BindView(R.id.img_leftarrow)
    ImageView imgLeftarrow;
    List<StolenBikeLocation> stolenBikeLocations = new LinkedList<>();
    String qrcode = "";
    String status = "";
    String userid = "";
    String mobile;
    String owner;
    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    Location location;
    Intent intent;
    List<String> cordinatesStolenBike = new ArrayList<>();
    private double currentlatitude;
    private double currentlongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stolen_bike_drawer);
        ButterKnife.bind(this);
        settingdrawerlayout();
        settingUserName();
        View view = findViewById(R.id.include_layout);
        FrameLayout btnReport = view.findViewById(R.id.btn_report);
        getLocation();
        if (getIntent().getStringExtra("qrcode") != null) {
            qrcode = getIntent().getStringExtra("qrcode");
        }
        if (getIntent().getStringExtra("status") != null) {
            status = getIntent().getStringExtra("status");
        }
        if (getIntent().getStringExtra("userid") != null) {
            userid = getIntent().getStringExtra("userid");
        }
        intent = new Intent(this, NewStolenBikeActivity.class);
       if(Singleton.getInstance().isloginFrom.equals("police"))
        {
            if (getIntent().getStringExtra("mobile") != null) {
                mobile = getIntent().getStringExtra("mobile");
            }
            if (getIntent().getStringExtra("owner") != null) {
                owner = getIntent().getStringExtra("owner");
            }

                textViewContact.setText("Contact the owner "+owner + "on their mobile number "+mobile);


        }

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void settingdrawerlayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (!Singleton.getInstance().getIsloginFrom().isEmpty()) {

            if (Singleton.getInstance().getIsloginFrom().equals("police")) {

                navigationView.inflateMenu(R.menu.police_menu);

            } else if (Singleton.getInstance().getIsloginFrom().equals("user")) {
                navigationView.inflateMenu(R.menu.menu);
            }
        }
    }

    private void settingUserName() {
        SharedPreferences prefs = getSharedPreferences("veloeye", MODE_PRIVATE);
        textviewUsername.setText("Username : " + prefs.getString("username", null));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("MissingPermission")
    public Location getLocation() {
        try {
            locationManager = (LocationManager) this
                    .getSystemService(LOCATION_SERVICE);
            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            if (!isGPSEnabled && !isNetworkEnabled) {
                // no network provider is enabled
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network");
                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            currentlatitude = location.getLatitude();
                            currentlongitude = location.getLongitude();
                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
                        Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                currentlatitude = location.getLatitude();
                                currentlongitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    private void getOwnerStolenmap() {

        final MaterialDialog progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.please_wait)
                .progress(true, 0)
                .show();
        progressDialog.setCancelable(false);
        veloeyeAPI.stolenscans(userid, qrcode, currentlongitude, currentlatitude, new Callback<List<StolenScansResponse>>() {


            @Override
            public void success(List<StolenScansResponse> stolenScansResponses, Response response) {
                progressDialog.dismiss();
                if (!stolenScansResponses.get(0).equals(null)) {
                    cordinatesStolenBike.clear();
                    Log.d("cordinatearray", stolenScansResponses.size() + "");
                    for (int i = 0; i < stolenScansResponses.size(); i++) {
                        StolenScansResponse stolenScansResponse = stolenScansResponses.get(i);
                        Log.d("cordinatearrayloop", stolenScansResponses.size() + "");
                        Toast.makeText(StolenBikeActivity.this, "Successfully Reported", Toast.LENGTH_SHORT).show();
                        String cordinates = stolenScansResponse.getLat() + "," + stolenScansResponse.getLng();
                        cordinatesStolenBike.add(cordinates);


                        Log.d("cordinatesStolenBike", cordinatesStolenBike.size() + "");


                    }

                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                showErrorDialog(R.string.error, R.string.server_not_responding);
                Log.e("NEWBIKE", error.getMessage());
            }
        });
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


    private void apiCallForStolenBike(StolenBikeLocation stolenBikeLocation) {

        final MaterialDialog progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.please_wait)
                .progress(true, 0)
                .show();
        progressDialog.setCancelable(false);
        veloeyeAPI.apiCallForStolenBike(userid, status, qrcode, currentlongitude, currentlatitude, new Callback<List<StolenBikeLocation>>() {


            @Override
            public void success(List<StolenBikeLocation> stolenBikeLocations, Response response) {
                if (response.toString().equals("1")) {
                    Toast.makeText(StolenBikeActivity.this, "Thankyou", Toast.LENGTH_SHORT).show();
                    getOwnerStolenmap();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                showErrorDialog(R.string.error, R.string.server_not_responding);
                Log.e("Bikes", error.toString());
            }
        });
    }


    protected void showErrorDialog(int resIdTitle, int resIdContent) {
        new MaterialDialog.Builder(this)
                .title(resIdTitle)
                .content(resIdContent)
                .neutralText("OK")
                .show();
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @OnClick({R.id.img_leftarrow, R.id.btn_report})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_leftarrow:
                startActivity(ScanActivity.class);
                break;
            case R.id.btn_report:
                StolenBikeLocation stolenBikeLocation = new StolenBikeLocation();
                apiCallForStolenBike(stolenBikeLocation);
        }


    }

}