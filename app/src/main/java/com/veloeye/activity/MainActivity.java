package com.veloeye.activity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.veloeye.R;
import com.veloeye.Singleton.Singleton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    @BindView(R.id.imageScan)
    ImageView imageScan;
    @BindView(R.id.imageBike)
    ImageView imageBike;
    @BindView(R.id.imageView_stolen)
    ImageView imageViewStolen;
    @BindView(R.id.imageview_profile)
    ImageView imageviewProfile;
    @BindView(R.id.imageview_social)
    ImageView imageviewSocial;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.ll_icon)
    LinearLayout llIcon;
    ImageView imageViewPoliceScan;

    Bundle extras;
    @BindView(R.id.btnBikes)
    LinearLayout btnBikes;
    @BindView(R.id.ll_profile)
    LinearLayout llProfile;
    @BindView(R.id.ll_stolen)
    LinearLayout llStolen;
    @BindView(R.id.ll_social)
    LinearLayout llSocial;
    @BindView(R.id.imageview_stickers)
    ImageView imageviewStickers;
    @BindView(R.id.linear_layout_parent)
    LinearLayout linearLayoutParent;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.btnScan)
    LinearLayout btnScan;
    @BindView(R.id.ll_contact)
    LinearLayout llContact;
    @BindView(R.id.textview_police)
    TextView textViewPolice;
    @BindView(R.id.btm_facebook)
    ImageView btmFacebook;
    @BindView(R.id.btm_instagram)
    ImageView btmInstagram;
    @BindView(R.id.btm_twitter)
    ImageView btmTwitter;
    @BindView(R.id.btm_youtube)
    ImageView btmYoutube;
    @BindView(R.id.ll_btm_icon)
    LinearLayout llBtmIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        settingDrawerLayout();
        navView.setNavigationItemSelectedListener(this);
        navView.setItemIconTintList(null);
        requestPermissions();
        textViewPolice.setVisibility(View.VISIBLE);


        if (!Singleton.getInstance().getIsloginFrom().equals("")) {
            if (Singleton.getInstance().getIsloginFrom().equals("police")) {
                policeLogin();
                llIcon.setVisibility(View.GONE);

            } else if (Singleton.getInstance().getIsloginFrom().equals("user")) {
                navView.inflateMenu(R.menu.menu);
                settingUserName();
                llBtmIcon.setVisibility(View.VISIBLE);
                llIcon.setVisibility(View.VISIBLE);
            }


        }
    }

    private void policeLogin() {

        if (!Singleton.getInstance().getIsloginFrom().isEmpty()) ;
        {

            if (Singleton.getInstance().getIsloginFrom().equals("police")) {
                navView.inflateMenu(R.menu.police_menu);
                settingUserName();
                llIcon.setVisibility(View.GONE);
                View child = getLayoutInflater().inflate(R.layout.police_dashboard, null);
                LinearLayout linearLayoutParent = findViewById(R.id.linear_layout_parent);
                LinearLayout linearLayoutStolenPolice = child.findViewById(R.id.ll_stolen_police);
                LinearLayout linearLayoutProfilePolice = child.findViewById(R.id.ll_profile_police);
                LinearLayout linearLayoutContactPolice = child.findViewById(R.id.ll_contact_police);
                linearLayoutParent.addView(child);
                ImageView imageViewScanPolice = child.findViewById(R.id.imageView_scan_police);
                imageViewScanPolice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        startActivity(ScanActivity.class);
                    }
                });
                linearLayoutProfilePolice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        startActivity(ProfileActivity.class);
                    }
                });
                linearLayoutStolenPolice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        startActivity(NewStolenBikeActivity.class);
                    }
                });
                linearLayoutContactPolice.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        startActivity(ContactInfoActivity.class);
                    }
                });
                textViewPolice.setVisibility(View.GONE);

            }
        }
    }


    private void settingUserName() {

        tvUsername.setText("Loggedin as : " + prefs.getString("username", null));
    }

    private void settingDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


    }

    private void requestPermissions() {

        Dexter.withActivity(this).withPermissions(Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION).withListener(
                new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (!report.areAllPermissionsGranted()) {
                            Toast.makeText(MainActivity.this, R.string.perm_these, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                });
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }


    @SuppressWarnings("StatementWithEmptyBody")
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



            if (!Singleton.getInstance().getIsloginFrom().equals("")) {
                if (Singleton.getInstance().getIsloginFrom().equals("police")) {
                    Intent mainIntent = new Intent(getApplicationContext(), PoliceLoginActivity.class);
                    Bundle dataBundle = new Bundle();
                    dataBundle.putString("SIGNOUT", "1");
                    mainIntent.putExtras(dataBundle);
                    startActivity(mainIntent);
                }
                else if (Singleton.getInstance().getIsloginFrom().equals("user")) {
                    Intent mainIntent = new Intent(getApplicationContext(), LoginActivity.class);
                    Bundle dataBundle = new Bundle();
                    dataBundle.putString("SIGNOUT", "1");
                    mainIntent.putExtras(dataBundle);
                    startActivity(mainIntent);
                }
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @OnClick({R.id.btnBikes, R.id.ll_profile, R.id.ll_stolen, R.id.ll_social, R.id.ll_contact, R.id.btnScan, R.id.btm_facebook, R.id.btm_instagram, R.id.btm_twitter, R.id.btm_youtube})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnScan:
                startActivity(ScanActivity.class);
                break;
            case R.id.btnBikes:
                startActivity(BikesActivity.class);
                break;
            case R.id.ll_stolen:
                startActivity(NewStolenBikeActivity.class);
                break;
            case R.id.ll_profile:
                startActivity(ProfileActivity.class);
                break;
            case R.id.ll_contact:
                startActivity(ContactInfoActivity.class);
                break;
            case R.id.ll_social:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.veloeye.com"));
                startActivity(browserIntent);
                break;
            case R.id.btm_facebook:
                Intent fbIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/veloeyeapp/"));
                startActivity(fbIntent);

                break;
            case R.id.btm_instagram:
                Intent instaIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/veloeye/"));
                startActivity(instaIntent);
                break;
            case R.id.btm_twitter:
                Intent twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/veloeye"));
                startActivity(twitterIntent);
                break;
            case R.id.btm_youtube:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCw6KqEhyydCQVrA0GSzaoeg")));

                break;
        }

    }


    @OnClick(R.id.textview_police)
    public void onViewClicked() {
        startActivity(PoliceLoginActivity.class);
    }


}
