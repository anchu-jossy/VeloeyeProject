package com.veloeye.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.veloeye.R;
import com.veloeye.Singleton.Singleton;
import com.veloeye.api.ApiManager;
import com.veloeye.api.VeloeyeAPI;
import com.veloeye.model.data.StolenBike;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;




public class NewStolenBikeActivity extends FragmentActivity implements NavigationView.OnNavigationItemSelectedListener, LocationListener {

    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    SharedPreferences prefs;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.dashboard_id)
    LinearLayout dashboardId;
    private GoogleMap mMap;
    // Might be null if Google Play services APK is not available.
    Location location; // location
    private double currentlatitude;
    private double currentlongitude;
    SharedPreferences sharedPreferences;
    String userid = "";
    protected LocationManager locationManager;
    // flag for GPS status
    boolean isGPSEnabled = false;
    // flag for network status
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    ArrayList<String> cordinates_arrays;
    List<String> cordinatesStolenBike = new ArrayList<>();
    VeloeyeAPI veloeyeAPI;
    StolenBike stolenBike;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity_stolen_bike_drawer);
        ButterKnife.bind(this);

        settingDrawerLayout();
        veloeyeAPI = ApiManager.getApiInstance();
        getData();
        getLocation();
        getstolenlocal();
        settingUserName();
    }

    private void getData() {
        prefs = getSharedPreferences("veloeye", MODE_PRIVATE);
        userid = prefs.getString("userid", null);
        cordinates_arrays = getIntent().getStringArrayListExtra("CORDINATES_ARRAY");
    }

    @Override
    protected void onResume() {
        super.onResume();



    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            startActivity(new Intent(this,ProfileActivity.class));
            // Handle the camera action
        } else if (id == R.id.nav_mybikes) {
            startActivity(new Intent(this,BikesActivity.class));
        } else if (id == R.id.nav_scan) {
            startActivity(new Intent(this,ScanActivity.class));
        } else if (id == R.id.nav_facebook) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/"));
            startActivity(browserIntent);
        } else if (id == R.id.nav_twitter) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.twitter.com/"));
            startActivity(browserIntent);
        } else if (id == R.id.nav_instagram) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.instagram.com/"));
            startActivity(browserIntent);
        }
        else if (id == R.id.nav_youtube) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/"));
            startActivity(browserIntent);
        }
        else if (id == R.id.nav_website) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.veloeye.com/"));
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
    private void settingDrawerLayout() {
        navView.setNavigationItemSelectedListener(this);
        navView.setItemIconTintList(null);
        if (!Singleton.getInstance().getIsloginFrom().isEmpty()) {

            if (Singleton.getInstance().getIsloginFrom().equals("police")) {

                navView.inflateMenu(R.menu.police_menu);

            } else if (Singleton.getInstance().getIsloginFrom().equals("user")) {
                navView.inflateMenu(R.menu.menu);
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

    }


    private void settingUserName() {
        SharedPreferences prefs = getSharedPreferences("veloeye", MODE_PRIVATE);
        tvUsername.setText("Username : " + prefs.getString("username", null));
    }

    private void settingPoliceUserName() {
        SharedPreferences prefs = getSharedPreferences("veloeye", MODE_PRIVATE);
        tvUsername.setText("Username : " + prefs.getString("username", null));
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                    @Override
                    public void onMapLoaded() {

                        setUpMap();

                    }
                });
            }
        }
    }


    private void setUpMap() {
        for (String string : cordinatesStolenBike) {
            Log.d("insidesetup",cordinatesStolenBike.size()+"");
            String value = string;
            if (value.contains(",")) {
                String[] array = value.split(",");
                if (array.length == 2) {
                    double latitude = Double.parseDouble(array[0]);
                    double longitude = Double.parseDouble(array[1]);
                    LatLng latLng = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(stolenBike.getBikeinfo())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.red_bike_marker)));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f));
                }
            }
        }

    }


    private void getstolenlocal() {

        final MaterialDialog progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.please_wait)
                .progress(true, 0)
                .show();
        progressDialog.setCancelable(false);
        veloeyeAPI.stolenlocal(userid, currentlongitude, currentlatitude, new Callback<List<StolenBike>>() {


            @Override
            public void success(List<StolenBike> stolenBikes, Response response) {

                progressDialog.dismiss();

              Log.d("insideapi",response.toString());
                if (cordinatesStolenBike != null) {
                    cordinatesStolenBike.clear();
                    for (int i = 0; i < stolenBikes.size(); i++) {

                        stolenBike = stolenBikes.get(i);
                        String cordinates = stolenBike.getLat() + "," + stolenBike.getLng();
                        cordinatesStolenBike.add(cordinates);
                        Log.d("insideapis",cordinatesStolenBike.size()+"");
                        if (cordinatesStolenBike != null) {

                            setUpMapIfNeeded();

                        }

                    }
                }

            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                Log.e("newstolen", error + "");
            }
        });
    }


    @Override
    public void onLocationChanged(Location location) {


    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

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
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
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

    @OnClick(R.id.dashboard_id)
    public void onViewClicked() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
      /*  Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);*/
    }


}
