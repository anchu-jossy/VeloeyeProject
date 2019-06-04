package com.veloeye.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.veloeye.R;
import com.veloeye.adapter.BikesAdapter;
import com.veloeye.adapter.CommonInterface;
import com.veloeye.model.data.Bike;

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
public class BikesActivity extends AppActivity implements CommonInterface {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.dashboard_id)
    LinearLayout dashboard_id;
    private BikesAdapter bikesAdapter;
    List<Bike> bikeList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bikes);
        ButterKnife.bind(this);
        setRecyclerAdapter();
        requestPermissions();
    }

    private void setRecyclerAdapter() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bikesAdapter = new BikesAdapter(this, bikeList, this);
        recyclerView.setAdapter(bikesAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getBikes();
    }


    @Override
    public void delete(Bike bike) {
        Intent intent = new Intent(BikesActivity.this, DeleteBikeActivity.class);
        intent.putExtra("Bike_Image", bike.getImages().get(0).getImg());
        intent.putExtra("bike", bike);

        startActivity(intent);
    }

    private void getBikes() {
        final MaterialDialog progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.please_wait)
                .progress(true, 0)
                .show();
        progressDialog.setCancelable(false);
        veloeyeAPI.getBikes(prefs.getString("userid", ""), new Callback<List<Bike>>() {
            @Override
            public void success(List<Bike> bikes, Response response) {


                if (bikes != null) {
                    progressDialog.dismiss();
                    bikeList.clear();
                    bikeList.addAll(bikes);
                }

                bikesAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                showErrorDialog(R.string.error, R.string.server_not_responding);
                Log.e("Bikes", error.getMessage());
            }
        });
    }


    private void requestPermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION

                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (!report.areAllPermissionsGranted()) {
                    Toast.makeText(BikesActivity.this, R.string.perm_location, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }

        }).check();


    }

    @OnClick(R.id.dashboard_id)
    public void onViewClicked() {
        Intent intent = new Intent(BikesActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
