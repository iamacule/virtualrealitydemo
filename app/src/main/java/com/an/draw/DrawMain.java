package com.an.draw;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import com.an.ViewActivity;

/**
 * Created by MrAn on 19-Apr-16.
 */
public class DrawMain extends View{
    private DrawFocus drawFocus;
    private DrawModel drawModel;

    public DrawMain(Context context, Activity activity) {
        super(context);
        drawFocus = new DrawFocus(activity);
        drawModel = new DrawModel(activity);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawFocus.draw(canvas);
        drawModel.draw(canvas);
        drawModel.update(ViewActivity.defaultModel);
        invalidate();
    }
}
