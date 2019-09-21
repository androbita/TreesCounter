package com.ngopidev.project.treescounter.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;
import com.ngopidev.project.treescounter.EncodeQr.Contents;
import com.ngopidev.project.treescounter.EncodeQr.QRCodeEncoder;
import com.ngopidev.project.treescounter.Model.Trees;
import com.ngopidev.project.treescounter.R;
import com.ngopidev.project.treescounter.helpers.AllConstHere;
import com.ngopidev.project.treescounter.helpers.PrefsHelper;
import com.ngopidev.project.treescounter.networking.ApiOnly;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * created by Irfan Assidiq on 2019-09-10
 * email : assidiq.irfan@gmail.com
 **/


public class UploadActivity extends AppCompatActivity {

    int pickPhoto = 1;
    TextView txtAddImg;
    ImageView imgView, imgQr;
    Spinner spinInject;
    Button btnSubmit;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_data_trees);
        PrefsHelper data = new PrefsHelper(getApplicationContext());
        String nama = data.getToken();
        boolean sample = data.getLoginStat();

        final EditText etNumber, etNames, etHeight, etDiameter, etLocation, etOwner, etInjector;

        etNumber = findViewById(R.id.et_treeNumbers);
        etNames = findViewById(R.id.et_treeNames);
        etHeight = findViewById(R.id.et_treeHeight);
        etDiameter = findViewById(R.id.et_treeDiameter);
        etLocation = findViewById(R.id.et_treeLocation);
        etOwner = findViewById(R.id.et_treeOwner);
        etInjector = findViewById(R.id.et_treeInjector);
        txtAddImg = findViewById(R.id.tv_addimage);
        spinInject = findViewById(R.id.spintreeInjection);
        btnSubmit = findViewById(R.id.btn_submit);

        String[] dataList = getResources().getStringArray(R.array.list_data);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.for_spinner, dataList);
        spinInject.setAdapter(adapter);

        txtAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgView = findViewById(R.id.img_addimage);
                if (((BitmapDrawable) imgView.getDrawable()).getBitmap().equals(null)) {
                    Toast.makeText(UploadActivity.this, "Please Insert Photo", Toast.LENGTH_SHORT).show();
                }else {

                    Bitmap bitmap = ((BitmapDrawable) imgView.getDrawable()).getBitmap();

                    String number = etNumber.getText().toString();
                    String names = etNames.getText().toString();
                    String height = etHeight.getText().toString();
                    String diameter = etDiameter.getText().toString();
                    String location = etLocation.getText().toString();
                    String owner = etOwner.getText().toString();
                    int spin = (int) spinInject.getSelectedItemId();
                    String injector = etInjector.getText().toString();

                    String encode = encodeToBase64(bitmap);

                    String qr = owner + "_" + number + "_" + names + ".png";

                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int heigh = point.y;
                    int smallerDimension = width < heigh ? width : heigh;
                    smallerDimension = smallerDimension * 3 / 4;


                    QRCodeEncoder qrCodeEncoder = new QRCodeEncoder(qr, null, Contents.Type.TEXT,
                            BarcodeFormat.QR_CODE.toString(), smallerDimension);

                    try {
                        Bitmap bitmapEncode = qrCodeEncoder.encodeAsBitmap();
                        ImageView myImage = (ImageView) findViewById(R.id.img_add_qr);
                        myImage.setImageBitmap(bitmapEncode);
                        String encodeQr = encodeToBase64(bitmapEncode);

                        insertData(number, names, height, diameter, location, owner, encode, spin, injector, encodeQr);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private String encodeToBase64(Bitmap image) {
        Bitmap imgEncode = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imgEncode.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imgEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        return imgEncoded;
    }

    private void insertData(String number, String names, String height, String diameter, String location, String owner, String encode, int spin, String injector, String encodeQr) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setLenient();
        Gson gson = gsonBuilder.create();

        Retrofit builder = new Retrofit.Builder().baseUrl(AllConstHere.baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiOnly apiOnly = builder.create(ApiOnly.class);


        if (!number.isEmpty() || !names.isEmpty() || !height.isEmpty() || !diameter.isEmpty() || !location.isEmpty() || !owner.isEmpty() || !encode.isEmpty() || spin > 0 || !injector.isEmpty()){
            String convertSpin = String.valueOf(spin);

            Call<List<UploadActivity>> call = apiOnly.uploadData(number, names, height, diameter, location, encode, convertSpin, injector, owner, encodeQr);
            call.enqueue((new Callback<List<UploadActivity>>() {
                @Override
                public void onResponse(Call<List<UploadActivity>> call, Response<List<UploadActivity>> response) {
                    Toast.makeText(UploadActivity.this, "Success Submit Data", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<List<UploadActivity>> call, Throwable t) {
                    Toast.makeText(UploadActivity.this, "Failed Insert Data", Toast.LENGTH_SHORT).show();
                }
            }));
        }
    }


    public void pickImage(){
        imgQr = findViewById(R.id.img_add_qr);
        imgView = findViewById(R.id.img_addimage);
        imgView.setVisibility(View.VISIBLE);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, pickPhoto);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pickPhoto && resultCode == Activity.RESULT_OK){
            if (data == null){
                return;
            }

            try {
//                InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(intent.getData());
                Uri selectedImageUri = data.getData();
                final String path = getPathFromURI(selectedImageUri);
                if (path != null){
                    File f = new File(path);
                    selectedImageUri = Uri.fromFile(f);
                }

                imgView.setImageURI(selectedImageUri);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private String getPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);

        if (cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }

        cursor.close();
        return res;
    }

//    private Bitmap decodeFile(File f) {
//        Bitmap b = null;
//
//        //Decode image size
//        BitmapFactory.Options o = new BitmapFactory.Options();
//        o.inJustDecodeBounds = true;
//
//        FileInputStream fis = null;
//        try {
//            fis = new FileInputStream(f);
//            BitmapFactory.decodeStream(fis, null, o);
//            fis.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        int IMAGE_MAX_SIZE = 1024;
//        int scale = 1;
//        if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
//            scale = (int) Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_SIZE /
//                    (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
//        }
//
//        //Decode with inSampleSize
//        BitmapFactory.Options o2 = new BitmapFactory.Options();
//        o2.inSampleSize = scale;
//        try {
//            fis = new FileInputStream(f);
//            b = BitmapFactory.decodeStream(fis, null, o2);
//            fis.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Log.d(TAG, "Width :" + b.getWidth() + " Height :" + b.getHeight());
//
//        destFile = new File(file, "img_"
//                + dateFormatter.format(new Date()).toString() + ".png");
//        try {
//            FileOutputStream out = new FileOutputStream(destFile);
//            b.compress(Bitmap.CompressFormat.PNG, 100, out);
//            out.flush();
//            out.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return b;
//    }
}
