package com.veloeye.fragment;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import com.veloeye.event.ScannedQrEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class SimpleScannerFragment extends Fragment implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private boolean resultSent = false;
    private boolean permissionChecked = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mScannerView = new ZXingScannerView(getActivity());

        List<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.QR_CODE);

        mScannerView.setFormats(formats);

        return mScannerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        resultSent = false;
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        if (!resultSent) {
            resultSent = true;
            String qrCode = String.valueOf(rawResult.getText());
            String qrCodePart = qrCode.substring(qrCode.length() -7);
            EventBus.getDefault().post(new ScannedQrEvent(qrCodePart));
        }

     /*  Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
           @Override
            public void run() {
               mScannerView.resumeCameraPreview(SimpleScannerFragment.this);
           }}, 2000);*/
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }
}