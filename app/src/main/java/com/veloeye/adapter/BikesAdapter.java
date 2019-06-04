package com.veloeye.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.veloeye.R;
import com.veloeye.Singleton.Singleton;
import com.veloeye.activity.REgistrationActivity;
import com.veloeye.activity.TransferBikeActivity;
import com.veloeye.api.ApiManager;
import com.veloeye.api.VeloeyeAPI;
import com.veloeye.model.data.Bike;
import com.veloeye.model.data.Image;
import com.veloeye.model.data.UpdateBikeResponse;
import com.weiwangcn.betterspinner.library.BetterSpinner;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Florin Mihalache on 14.09.2015.
 */
public class BikesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
    Bundle dataBundle;
    Intent registrationIntent;
    VeloeyeAPI veloeyeAPI;
    Bike bike;
    ViewHolder vh;
    Integer i;
    private List<Bike> list;
    private Context context;
    private EventBus eventBus;
    private CommonInterface commonInterface;

    public BikesAdapter(Context context, List<Bike> list, CommonInterface commonInterface) {
        this.context = context;
        this.list = list;
        eventBus = EventBus.getDefault();
        dataBundle = new Bundle();
        veloeyeAPI = ApiManager.getApiInstance();
        this.commonInterface = (CommonInterface) commonInterface;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        this.i = i;
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item_bike_new, viewGroup, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        vh = (ViewHolder) viewHolder;
        bike = list.get(i);

        setBikeImage();


        if (bike.getStatus().equals("3")) {

            clickForStolen(bike, vh);


        } else {
            clickforUnStolen(bike, vh);

        }


        vh.tvBikeName.setText(bike.getMakename());
        vh.tvmanufacturer.setText(bike.getModel());
        ViewHolder containerView = vh;

        vh.itemContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                alerDialogLongPress(containerView, list.get(i));

                return false;
            }
        });
    }

    private void setBikeImage() {
        if (bike.getImages().get(0).getImg() != null) {
            String image = bike.getImages().get(0).getImg();
            Glide
                    .with(context)
                    .load(image)
                    .centerCrop()
                    .into(vh.ivImage_new);
            Singleton.getInstance().setBikeimages(bike.getImages().get(0).getImg());
        }
    }

    private void alerDialogLongPress(ViewHolder vh, Bike bike) {

        new AlertDialog.Builder(context)
                .setTitle("Stolen")
                .setMessage("Are you sure you want to confirm your bike was stolen?")
                .setCancelable(true)
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        setBikeStolen(bike.getQrcode(), bike.getBikeid(), bike.getUserid(), "6", vh);

                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        setBikeStolen(bike.getQrcode(), bike.getBikeid(), bike.getUserid(), "3", vh);
                        clickForStolen(bike, vh);
                    }
                }).show();
    }

    private void clickforUnStolen(Bike bike, ViewHolder vh) {

        vh.itemContainer.setBackgroundResource(R.drawable.bg_mybikes);


        String[] items = new String[]{"Transfer", "Edit", "Delete", "Stolen"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, items);
        dataBundle.putString("BIKEID", bike.getBikeid());
        Intent mIntent = new Intent(context, TransferBikeActivity.class);
        mIntent.putExtras(dataBundle);
        vh.spinnerActions.setAdapter(adapter);
        vh.spinnerActions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    context.startActivity(mIntent);
                } else if (position == 1) {
                    registrationIntent = new Intent(context, REgistrationActivity.class);

                    registrationIntent.putExtra("bike", bike);





                    context.startActivity(registrationIntent);

                } else if (position == 2) {
                    commonInterface.delete(bike);

                } else if (position == 3)
                    setBikeStolen(bike.getQrcode(), bike.getBikeid(), bike.getUserid(), "3", vh);

            }
        });
    }

    private void clickForStolen(Bike bike, ViewHolder vh) {

        vh.itemContainer.setBackgroundResource(R.drawable.bg_mybikes_stolen);
        String[] items = new String[]{"Transfer","Delete","Recovered"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, items);
        dataBundle.putString("BIKEID", bike.getBikeid());
        Intent mIntent = new Intent(context, TransferBikeActivity.class);
        mIntent.putExtras(dataBundle);
        vh.spinnerActions.setAdapter(adapter);
        vh.spinnerActions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    context.startActivity(mIntent);
                } else if (position == 1) {
                    commonInterface.delete(bike);

                } else if (position == 2) {
                    setBikeStolen(bike.getQrcode(), bike.getBikeid(), bike.getUserid(), "6", vh);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected void showErrorDialog(int resIdTitle, int resIdContent) {
        new MaterialDialog.Builder(context)
                .title(resIdTitle)
                .content(resIdContent)
                .neutralText("OK")
                .show();
    }

    private void setBikeStolen(String qrcode, String bikeid, String userid, String status, ViewHolder vh) {
        final MaterialDialog progressDialog = new MaterialDialog.Builder(context)
                .content(R.string.please_wait)
                .progress(true, 0)
                .show();
        progressDialog.setCancelable(false);
        veloeyeAPI.updateBike(qrcode, bikeid, userid, status, new Callback<List<UpdateBikeResponse>>() {


            @Override
            public void success(List<UpdateBikeResponse> updateBikeResponsesList, Response response) {
                progressDialog.dismiss();
                if (updateBikeResponsesList.get(0).getResponse().equals("1")) {
                    if (updateBikeResponsesList.get(0).getBike_status().equals("3")) {
                        Toast.makeText(context, "Successfully updated", Toast.LENGTH_SHORT).show();
                        vh.itemContainer.setBackgroundResource(R.drawable.bg_mybikes_stolen);
                    } else {
                        Toast.makeText(context, "Successfully updated", Toast.LENGTH_SHORT).show();
                        clickforUnStolen(bike, vh);
                    }

                }
                else
                    Toast.makeText(context, "failed to update", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                progressDialog.dismiss();
                showErrorDialog(R.string.error, R.string.server_not_responding);
                Log.e("BikesAdapter", error.getMessage());
            }
        });

    }

    /* public void updateBikeStatus(int position, String newStatus) {
         list.get(position).setStatus(newStatus);
         notifyItemChanged(position);
     }
 */
    class ViewHolder extends RecyclerView.ViewHolder {
        public @BindView(R.id.ivImage_new)
        ImageView ivImage_new;
        public @BindView(R.id.textview_bikename)
        TextView tvBikeName;
        public @BindView(R.id.textView_manufacturer)
        TextView tvmanufacturer;
        public @BindView(R.id.itemContainer)
        CardView itemContainer;
        @BindView(R.id.spinner_actions)
        BetterSpinner spinnerActions;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
