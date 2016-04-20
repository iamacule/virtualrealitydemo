package com.an;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.an.util.DataUtil;
import com.an.util.ScreenUtil;

public class MainActivity extends AppCompatActivity {
    private Button btnStart;
    private MainActivity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivity = this;
        DataUtil.screenWidth = ScreenUtil.getScreenWidth(getWindowManager());
        DataUtil.screenHeight = ScreenUtil.getScreenHeight(getWindowManager());
        btnStart = (Button)findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mainActivity,ViewActivity.class));
            }
        });
    }
}
