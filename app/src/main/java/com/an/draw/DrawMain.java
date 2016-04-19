package com.an.draw;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

/**
 * Created by MrAn on 19-Apr-16.
 */
public class DrawMain extends View{
    private DrawFocus drawFocus;

    public DrawMain(Context context, Activity activity) {
        super(context);
        drawFocus = new DrawFocus(activity);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawFocus.draw(canvas);
    }
}
