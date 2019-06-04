package com.veloeye.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.veloeye.R;
import com.veloeye.Singleton.Singleton;
import com.veloeye.model.data.Bike;
import com.veloeye.model.data.DeleteResponse;
import com.veloeye.model.data.Manufacturer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class DeleteBikeActivity extends AppActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.imgView_police)
    ImageView imgViewPolice;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.textView_enter)
    TextView textViewEnter;
    @BindView(R.id.ivImage)
    ImageView ivImage;
    @BindView(R.id.textview_bikename)
    TextView textviewBikename;
    @BindView(R.id.textView_manufacturer)
    TextView textViewManufacturer;
    @BindView(R.id.textView_color)
    TextView textViewColor;
    @BindView(R.id.ll_bikelist)
    LinearLayout llBikelist;
    @BindView(R.id.itemContainer)
    CardView itemContainer;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.linearLayout2)
    LinearLayout linearLayout2;
    @BindView(R.id.rl_parent_login)
    RelativeLayout rlParentLogin;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.btnTransfer)
    FrameLayout btnTransfer;
    String bike_Imagename = "";
    List<Manufacturer> manufacturersList;
    CharSequence[] manCharSequenceList;
    int count = 0;
    int manID = 0;
    Bike bike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletion_drawer);
        ButterKnife.bind(this);
        settingDrawerLayout();
        getManufacturersList();
        getIntentData();
        settingData();

    }

    private void settingData() {
        if (!bike_Imagename.equals("")) {
            Glide
                    .with(DeleteBikeActivity.this)
                    .load(bike_Imagename)
                    .centerCrop()
                    .into(ivImage);

        }
        if (!bike.getColor().equals("")) {
            textViewColor.setText(bike.getColor());
        }
        if (!bike.getModel().equals("")) {
            textViewManufacturer.setText(bike.getModel());
        }
        if (!bike.getMakename().equals("")) {
            textviewBikename.setText(bike.getMakename());
        }
    }

    private void getIntentData() {
        if (getIntent().getStringExtra("Bike_Image") != null) {
            bike_Imagename = getIntent().getStringExtra("Bike_Image");
        }

        if (getIntent().getParcelableExtra("bike") != null) {
            bike = (Bike) getIntent().getParcelableExtra("bike");
        }


        if (bike.getStatus().equals("3")) {
            itemContainer.setBackgroundResource(R.drawable.bg_mybikes_stolen);
        } else
            itemContainer.setBackgroundResource(R.drawable.bg_mybikes);
    }

    private void settingDrawerLayout() {
        navView.setNavigationItemSelectedListener(this);
        navView.setItemIconTintList(null);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (!Singleton.getInstance().getIsloginFrom().isEmpty()) {

            if (Singleton.getInstance().getIsloginFrom().equals("police")) {

                navView.inflateMenu(R.menu.police_menu);

            } else if (Singleton.getInstance().getIsloginFrom().equals("user")) {
                navView.inflateMenu(R.menu.menu);
            }
        }

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

    private void getDeleteBike() {
        final MaterialDialog progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.please_wait)
                .progress(true, 0)
                .show();
        progressDialog.setCancelable(false);
        veloeyeAPI.deleteBike(bike.getQrcode(), bike.getBikeid(), bike.getUserid(), manID, bike.getModel(), bike.getColor(), "2019", bike.getFramecode(), "4", new Callback<List<DeleteResponse>>() {
            @Override
            public void success(List<DeleteResponse> deleteResponses, Response response) {
                progressDialog.dismiss();
                if (deleteResponses.get(0).getResponse().equals("1")) {
                    Toast.makeText(DeleteBikeActivity.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
                    startActivity(MainActivity.class);
                } else {
                    Toast.makeText(DeleteBikeActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();

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


    private void getPositionList() {
        for (Manufacturer manufacturer : manufacturersList) {
            if (textviewBikename.getText().equals(manufacturer.getManufacturer())) {
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

    @OnClick(R.id.btnTransfer)
    public void onViewClicked() {

        getPositionList();
        getDeleteBike();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
