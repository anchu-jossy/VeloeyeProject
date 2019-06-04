package com.veloeye.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.google.android.gms.maps.GoogleMap;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.veloeye.R;
import com.veloeye.Singleton.Singleton;
import com.veloeye.api.ApiManager;
import com.veloeye.api.VeloeyeAPI;
import com.veloeye.event.ScannedQrEvent;
import com.veloeye.model.data.ScanResponse;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesWithFallbackProvider;
import io.nlopez.smartlocation.location.utils.LocationState;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by Florin on 11.09.2015.
 */
public class ScanActivity extends FragmentActivity {
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
//    @BindView(R.id.tv_username)
//    TextView tvUsername;
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager locationManager;
    SharedPreferences prefs;
    @BindView(R.id.dashboard_id)
    LinearLayout dashboard_id;
    // Might be null if Google Play services APK is not available.
    Location location; // location
    SharedPreferences sharedPreferences;
    String userid = "";
    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    private GoogleMap mMap;
    private double currentlatitude;
    private double currentlongitude;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        ButterKnife.bind(this);
        requestPermissions();
        prefs = getSharedPreferences("veloeye", MODE_PRIVATE);
        EventBus.getDefault().register(this);
        getLocation();


        dashboard_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScanActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void requestPermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION


                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (!report.areAllPermissionsGranted()) {
                    Toast.makeText(ScanActivity.this, R.string.perm_camera_location, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }

        }).check();


    }


    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final ScannedQrEvent event) {
        if (!isLocationEnabled()) {
            return;
        }

        final VeloeyeAPI veloeyeAPI = ApiManager.getApiInstance();

        prefs.edit()
                .putString("QRCODE", event.qrcode).apply();

        final MaterialDialog progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.please_wait)
                .progress(true, 0)
                .theme(Theme.LIGHT)
                .show();
        progressDialog.setCancelable(false);

        SmartLocation.with(this)
                .location(new LocationGooglePlayServicesWithFallbackProvider(this))
                .oneFix()
                .config(LocationParams.NAVIGATION)
                .start(new OnLocationUpdatedListener() {
                    @Override
                    public void onLocationUpdated(final Location location) {
                        veloeyeAPI.sendQrCode(
                                prefs.getString("userid", ""),
                                event.qrcode,
                                currentlatitude,
                                currentlongitude,
                                Integer.parseInt(prefs.getString("auth", "")),
                                new Callback<List<ScanResponse>>() {
                                    @Override
                                    public void success(List<ScanResponse> scanResponses, Response response) {
                                        progressDialog.dismiss();

                                        if (scanResponses.isEmpty()) {
                                            Toast.makeText(ScanActivity.this, R.string.sticker_not_found,
                                                    Toast.LENGTH_LONG).show();
                                            return;
                                        }

                                        if (scanResponses == null) {
                                            showErrorDialog(R.string.error, R.string.sending_data_wrong);
                                        } else {
                                            ScanResponse scanResponse = scanResponses.get(0);
                                            String status = scanResponse.getStatus();
                                            Intent intent;
//reg,active,stolen
                                            if (status.equals("1")) {
                                                intent = new Intent(ScanActivity.this, REgistrationActivity.class);
                                                intent.putExtra("qrcode", event.qrcode);
                                                startActivity(intent);
                                                finish();
                                            } else if (status.equals("2")) {
                                                /*intent = new Intent(ScanActivity.this, REgistrationActivity.class);
                                                intent.putExtra("qrcode", event.qrcode);
                                                startActivity(intent);
                                                finish();*/
                                                startActivity(ActiveBikeActivity.class);
                                                finish();
                                               /* intent = new Intent(ScanActivity.this, StolenBikeActivity.class);
                                                intent.putExtra("qrcode", event.qrcode);
                                                intent.putExtra("status", scanResponses.get(0).getStatus());
                                                intent.putExtra("userid", scanResponses.get(0).getUserid());
                                                intent.putExtra("latitude", currentlatitude);
                                                intent.putExtra("longitude", currentlongitude);
                                                startActivity(intent);
                                                finish();
*/
                                            } else if (status.equals("3")) {


                                                intent = new Intent(ScanActivity.this, StolenBikeActivity.class);
                                                intent.putExtra("qrcode", event.qrcode);
                                                intent.putExtra("status", scanResponse.getStatus());
                                                intent.putExtra("userid", scanResponse.getUserid());
                                                intent.putExtra("latitude", currentlatitude);
                                                intent.putExtra("longitude", currentlongitude);
                                                if (Singleton.getInstance().isloginFrom.equals("police")) {
                                                    intent.putExtra("owner", scanResponse.getOwner());
                                                    intent.putExtra("mobile", scanResponse.getMobile());
                                                }
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        progressDialog.dismiss();
                                        showErrorDialog(R.string.error, R.string.server_not_responding);
                                        Log.e("ScanQR", error.getMessage());
                                    }
                                });
                    }
                });


    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    protected void showErrorDialog(int resIdTitle, int resIdContent) {
        new MaterialDialog.Builder(this)
                .title(resIdTitle)
                .content(resIdContent)
                .neutralText("OK")
                .show();
    }

    private boolean isLocationEnabled() {
        LocationState state = SmartLocation.with(this).location().state();
        if (!state.locationServicesEnabled() || !state.isGpsAvailable()) {

            new MaterialDialog.Builder(this)
                    .title(R.string.app_name)
                    .content(getString(R.string.location_disabled))
                    .positiveText(R.string.ok)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            super.onPositive(dialog);
                            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .show();

            return false;
        }
        return true;
    }

    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
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
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);
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

}
