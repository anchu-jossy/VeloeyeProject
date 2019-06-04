package com.veloeye.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonArray;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.veloeye.R;
import com.veloeye.Singleton.Singleton;
import com.veloeye.adapter.ImagePickerAdapter;
import com.veloeye.model.data.AddBikeResponse;
import com.veloeye.model.data.Bike;
import com.veloeye.model.data.Manufacturer;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.Pivot;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ImageUploading extends AppActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private static final int SELECT_FILE = 1;
    private static final int TAKE_PHOTO = 1;
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    static String tempImagePath;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.textView_photos)
    TextView textViewPhotos;
    @BindView(R.id.ll_addbike)
    RelativeLayout llAddbike;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.btn_done)
    FrameLayout btnDone;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.ll_take_photo)
    LinearLayout llTakePhoto;
    @BindView(R.id.ll_image_upload)
    LinearLayout llImageUpload;
    File photoFile = null;
    String tempImagePath1;
    List<Manufacturer> manufacturersList;
    CharSequence[] manCharSequenceList;
    int selectedManufacturer = -1;
    String qrcode;
    ProgressDialog progressDialog;
    ArrayList<AddBikeResponse> addBikeResponseArrayList;
    @BindView(R.id.dashboard_id)
    LinearLayout dashboardId;
    @BindView(R.id.text)
    TextView text;
    /*@BindView(R.id.left_camera_Image)
    ImageView cameraImageLeft;*/
    /* @BindView(R.id.right_camera_Image)
     ImageView cameraImageRight;*/
    @BindView(R.id.image_Camera_gallary)
    LinearLayout imageCameraGallary;
    File test;
    File test1;
    Uri fileUri;
    Handler handler = new Handler();
    ArrayList<Uri> mArrayUri;
    ArrayList arrayList = new ArrayList<>();
    Uri selectedImage;

    Bike bike;
    @BindView(R.id.left_camera_arrow)
    ImageView leftCameraArrow;

    @BindView(R.id.right_camera_arrow)
    ImageView rightCameraArrow;
    @BindView(R.id.picker)
    DiscreteScrollView picker;


    private int MY_PERMISSION = 21;
    private String selectedImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_uploading);
        ButterKnife.bind(this);
        navView.setNavigationItemSelectedListener(this);
        navView.setItemIconTintList(null);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        if (!Singleton.getInstance().getIsloginFrom().equals("")) {

            if (Singleton.getInstance().getIsloginFrom().equals("police")) {

                navView.inflateMenu(R.menu.police_menu);

            } else if (Singleton.getInstance().getIsloginFrom().equals("user")) {
                navView.inflateMenu(R.menu.menu);
            }
        }
        Intent intent = getIntent();
        if (getIntent().getParcelableExtra("bike") != null) {
            bike = (Bike) getIntent().getParcelableExtra("bike");


            ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
           mArrayUri.add(Uri.parse( Singleton.getInstance().getBikeimages()));
            if (mArrayUri != null) {
                setImagePicker(mArrayUri);


            }

        }

        addBikeResponseArrayList = intent.getParcelableArrayListExtra("key");
        qrcode = getIntent().getStringExtra("QRCODE");
        LinearLayout linearLayout = toolbar.findViewById(R.id.dashboard_id);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(ImageUploading.this, MainActivity.class);
                startActivity(intent1);
                finish();
            }
        });

    }
    private void setImagePicker(ArrayList<Uri> infoArrayList) {
        for (int i = 0; i < infoArrayList.size(); i++) {
            Log.d("bikeimage", infoArrayList.size()+"");
            ImagePickerAdapter imagePickerAdapter = new ImagePickerAdapter(this, infoArrayList);
            picker.setAdapter(imagePickerAdapter);
            picker.setFocusable(true);
            picker.setOffscreenItems(infoArrayList.size()); //Reserve extra space equal to (childSize * count) on each side of the view
            picker.setOverScrollEnabled(true);
            picker.setItemTransformer(new ScaleTransformer.Builder()
                    .setMaxScale(1.00f)
                    .setMinScale(0.8f)
                    .setPivotX(Pivot.X.CENTER) // CENTER is a default one
                    .setPivotY(Pivot.Y.BOTTOM) // CENTER is a default one
                    .build());
        }

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
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

    @OnClick(R.id.btn_done)
    public void onViewClicked() {
        // finish();
        List<File> parts = new ArrayList<>();


        parts.add(test);

        if (getIntent().getParcelableExtra("bike") != null) {
            bike = (Bike) getIntent().getParcelableExtra("bike");


            callAPIEditBike(bike);



            return;
            //  addBikeResponseArrayList.add(getIntent().getParcelableExtra("bike"));

        } else if (getIntent().getParcelableArrayListExtra("key") != null) ;
        callAPI();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data == null && resultCode == 0)
            return;
        if (requestCode == TAKE_PHOTO) {
            if (resultCode == -1) {


                Uri selectedImage = fileUri;
                Log.d("requestCode11", selectedImage + "");
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (fileUri != null) {

                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    mArrayUri.add(fileUri);
                    if (mArrayUri != null) {
                        setImagePicker(mArrayUri);


                    }

                }
                Log.d("requestCode11bitmap", bitmap.toString() + "");

                Boolean flag = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                Log.d("requestCode11bitmap", bitmap.toString() + flag + "");

                test1 = bitmapToJpgFile(bitmap);
                Log.d("requestCode11bitmap", test1.getAbsolutePath() + flag + "");

            }
        } else if (requestCode == REQUEST_GALLERY) {
            Bitmap bitmap = null;
            if (resultCode == -1) {
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {

                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        mArrayUri.add(uri);

                        if (mArrayUri != null) {
                            setImagePicker(mArrayUri);
                            Log.d("123456", mArrayUri.size() + "");
                            //   cameraImageRight.setImageURI(selectedImage);
                        }
                        selectedImage = mArrayUri.get(i);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                      //  Boolean flag = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        test = bitmapToJpgFile(bitmap);
                        Log.d("testdata",test+"");
                    }

                }
                else if(data!=null){
                    Uri mImageUri=data.getData();
                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    mArrayUri.add(mImageUri);

                    if (mArrayUri != null) {
                        setImagePicker(mArrayUri);

                        //   cameraImageRight.setImageURI(selectedImage);
                    }

                    selectedImage = mArrayUri.get(0);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //  Boolean flag = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    test = bitmapToJpgFile(bitmap);
                    Log.d("testdata",test+"");

                }

/*
                        Boolean flag = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                        test = bitmapToJpgFile(bitmap);
                        Log.d("test11bitmap", test.getAbsolutePath() + flag + "");
                        Part part = new FilePart("image_name[" + i + "]", test);
                        arrayList.add((FilePart) part);*/

                //  }




/*
                String imagepath = selectedImagePath.toString();

                if (selectedImage != null) {

                    cameraImageLeft.setImageURI(selectedImage);
                 //   cameraImageRight.setImageURI(selectedImage);
                }
                if (data.getData() != null) {
                    cameraImageLeft.setImageURI(selectedImage);
                 //   cameraImageRight.setImageURI(data.getData());
                }*/
            }
        }

    }


    protected void dispatchTakePictureIntent() {


        //Check if there is a camera.
        PackageManager packageManager = getPackageManager();
        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Toast.makeText(this, "No camera available", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        // Camera exists? Then proceed...
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // startActivityForResult(takePictureIntent, TAKE_PHOTO);
        // Create the File where the photo should go.
        // If you don't do this, you may get a crash in some devices.

        try {
            photoFile = createImageFile();

        } catch (IOException ex) {
            ex.printStackTrace();
            // Error occurred while creating the File
            Toast toast = Toast.makeText(this, R.string.problem_saving_photo, Toast.LENGTH_SHORT);
            toast.show();
        }
        // Continue only if the File was successfully created
        if (photoFile != null) {
            tempImagePath = photoFile.getAbsolutePath();
            fileUri = Uri.fromFile(photoFile);
            prefs.edit()
                    .putString("photoFile", fileUri + "");
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(takePictureIntent, TAKE_PHOTO);

        }

    }

    protected File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        Log.d("REgistrationActivity", "storageDir.exists()");
        if (!storageDir.exists()) {
            storageDir.mkdir();
            Log.d("REgistrationActivity", "!storageDir.exists()");
        }

        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        Log.d("REgistrationActivity", " return storageDir.exists()");
        return image;
    }


    @OnClick({R.id.ll_take_photo, R.id.ll_image_upload, R.id.textView_photos, R.id.btn_done})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_take_photo:
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION);
                } else {
                    dispatchTakePictureIntent();
                }
                break;
            case R.id.ll_image_upload:
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSION);
                } else {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), REQUEST_GALLERY);
                }
                ;
                break;
            case R.id.textView_photos:
                break;

        }

    }

    private String getPath(Uri uri) {
        String result = "";
        Cursor cursor = ImageUploading.this.getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            result = uri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            if (idx != -1) {
                result = cursor.getString(idx);
            }
            cursor.close();
        }
        return result;
    }

    private void callAPI() {
        Log.d("callapi", "callAPI");
        final MaterialDialog progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.please_wait)
                .progress(true, 0)
                .show();
        progressDialog.setCancelable(false);
        if (test != null) {
            Ion.with(this)
                    .load("https://appservices.veloeye.com/imguploader.php")
                    .setMultipartParameter("code", qrcode)
                    .setMultipartParameter("bike", addBikeResponseArrayList.get(0).getBikeid())
                    .setMultipartFile("img", test)
                    .addMultipartParts(arrayList)
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {
                            JsonArray jsonArray = new JsonArray();


                            progressDialog.dismiss();
                            Log.d("TTTTTTT", "onCompleted: " + result);
                            Log.d("TTTTTTT", "onCompleted: " + e);
                            Toast.makeText(ImageUploading.this, "Image Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ImageUploading.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        } else if (test1 != null) {
            Ion.with(this)
                    .load("https://appservices.veloeye.com/imguploader.php")
                    .setMultipartParameter("code", qrcode)
                    .setMultipartParameter("bike", addBikeResponseArrayList.get(0).getBikeid())
                    .setMultipartFile("img", test1)

                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {

                            Toast.makeText(ImageUploading.this, "Image Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                            Log.d("TTT2", "onCompleted: " + result);
                            Log.d("TTT1", "onCompleted: " + e);
                            Intent intent = new Intent(ImageUploading.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        } else if (test1 == null) {
            Intent intent = new Intent(ImageUploading.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void callAPIEditBike(Bike bike) {
        Log.d("callapi", "callAPIEditBike");
        final MaterialDialog progressDialog = new MaterialDialog.Builder(this)
                .content(R.string.please_wait)
                .progress(true, 0)
                .show();
        progressDialog.setCancelable(false);
        if (test != null) {
            Ion.with(this)
                    .load("https://appservices.veloeye.com/imguploader.php")
                    .setMultipartParameter("code", bike.getQrcode())
                    .setMultipartParameter("bike", bike.getBikeid())
                    .setMultipartFile("img", test)
                    .addMultipartParts(arrayList)
                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {
                            JsonArray jsonArray = new JsonArray();


                            progressDialog.dismiss();
                            Log.d("TTTTTTT123", "onCompleted: " + result);
                            Log.d("TTTTTTT", "onCompleted: " + e);
                            Toast.makeText(ImageUploading.this, "Image Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ImageUploading.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        } else if (test1 != null) {
            Ion.with(this)
                    .load("https://appservices.veloeye.com/imguploader.php")
                    .setMultipartParameter("code", bike.getQrcode())
                    .setMultipartParameter("bike", bike.getBikeid())
                    .setMultipartFile("img", test1)

                    .asJsonArray()
                    .setCallback(new FutureCallback<JsonArray>() {
                        @Override
                        public void onCompleted(Exception e, JsonArray result) {

                            Toast.makeText(ImageUploading.this, "Image Uploaded Succesfully", Toast.LENGTH_SHORT).show();
                            Log.d("TTT123", "onCompleted: " + result);
                            Log.d("TTT123", "onCompleted: " + e);
                            Intent intent = new Intent(ImageUploading.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        } else if (test1 == null) {
            Intent intent = new Intent(ImageUploading.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public File bitmapToJpgFile(Bitmap bmp) {
        try {
            int size;
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            byte[] bArr = bos.toByteArray();
            bos.flush();
            bos.close();

            FileOutputStream fos = openFileOutput("mdroid.jpeg", Context.MODE_PRIVATE);
            fos.write(bArr);
            fos.flush();
            fos.close();

            File mFile = new File(getFilesDir().getAbsolutePath(), "mdroid.jpeg");
            Log.d("FileTest", mFile.getAbsolutePath());
            return mFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
