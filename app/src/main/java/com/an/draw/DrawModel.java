package com.an.draw;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;

import com.an.ViewActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrAn on 20-Apr-16.
 */
public class DrawModel implements Draw {
    private final Activity activity;
    private Bitmap mainModel;
    private List<Bitmap> listDynamicBP;

    public DrawModel(Activity activity) {
        this.activity = activity;
        mainModel = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        mainModel.eraseColor(Color.TRANSPARENT);
        listDynamicBP = new ArrayList<>();
    }
    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(mainModel, canvas.getWidth()/2-mainModel.getWidth()/2, canvas.getHeight()/2-mainModel.getHeight()/2, null);
    }

    @Override
    public void update() {

    }

    public void update(Bitmap bitmap){
        mainModel = bitmap;
    }
}
