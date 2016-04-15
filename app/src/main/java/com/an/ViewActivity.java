package com.an;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
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
            String data = arg1.getStringExtra(StreamServices.DATA_LABEL);
            Toast.makeText(viewActivity, data, Toast.LENGTH_LONG).show();
        }

    }
}
