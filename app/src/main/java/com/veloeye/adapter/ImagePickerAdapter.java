package com.veloeye.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.veloeye.R;
import com.veloeye.Singleton.Singleton;
import com.veloeye.activity.ImageUploading;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImagePickerAdapter extends RecyclerView.Adapter<ImagePickerAdapter.ViewHolder> {
    String imagepath;
    ArrayList<Uri> infoArrayList;
    Context context;
    Boolean ispreloaded;
    String image;

    public ImagePickerAdapter(Context context, ArrayList<Uri> infoArrayList) {
        this.infoArrayList = infoArrayList;
        this.context = context;
        Log.d("bikeimage",  infoArrayList.size()+"");
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_imagepicker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {


            Glide.with(context).load(infoArrayList.get(i)).into(viewHolder.imageView);

    }

    @Override
    public int getItemCount() {
        return infoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.imageView)
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);


            ButterKnife.bind(this, itemView);
        }


        @Override
        public void onClick(View v) {

        }
    }
}
