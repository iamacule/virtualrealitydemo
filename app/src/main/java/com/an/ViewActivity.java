package com.an;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.an.services.StreamServices;

/**
 * Created by MrAn on 15-Apr-16.
 */
public class ViewActivity extends AppCompatActivity {
    private ImageView imgPreview;
    private Button btnBack;
    private ViewActivity viewActivity;
    private DataReceiver dataReceiver;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        checkPermission();
        viewActivity = this;
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        startService();
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(viewActivity, "PermissionCamera granted !", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        dataReceiver = new DataReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(StreamServices.STRING_ACTION);
        registerReceiver(dataReceiver, intentFilter);
    }

    // Method to start the service
    public void startService() {
        startService(new Intent(getBaseContext(), StreamServices.class));
    }

    // Method to stop the service
    public void stopService() {
        stopService(new Intent(getBaseContext(), StreamServices.class));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(dataReceiver);
        stopService();
    }

    private class DataReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            // TODO Auto-generated method stub
            byte[] data = arg1.getByteArrayExtra(StreamServices.DATA_LABEL);
            Toast.makeText(viewActivity, data.toString(), Toast.LENGTH_LONG).show();
        }

    }
}
