package com.an.draw;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by MrAn on 19-Apr-16.
 */
public class DrawFocus implements Draw {
    private Activity activity;
    private Paint paintFocus;
    private Paint paintMidpoint;
    private int width;
    private int height;

    public DrawFocus(Activity activity) {
        this.activity = activity;
        paintFocus = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintFocus.setColor(Color.WHITE);
        paintFocus.setStyle(Paint.Style.FILL_AND_STROKE);
        paintMidpoint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintMidpoint.setColor(Color.WHITE);
        paintMidpoint.setStyle(Paint.Style.STROKE);
    }
    @Override
    public void draw(Canvas canvas) {
        width = canvas.getWidth();
        height = canvas.getHeight();
        drawData(canvas);
    }

    private void drawData(Canvas canvas) {
        //Draw left focus
        canvas.drawLine(width/4,height*3/8,width*3/8,height*3/8, paintFocus);
        canvas.drawLine(width/4,height*3/8,width/4,height*5/8, paintFocus);
        canvas.drawLine(width/4,height*5/8,width*3/8,height*5/8, paintFocus);

        //Draw right focus
        canvas.drawLine(width*5/8,height*3/8,width*3/4,height*3/8, paintFocus);
        canvas.drawLine(width*3/4,height*3/8,width*3/4,height*5/8, paintFocus);
        canvas.drawLine(width*5/8,height*5/8,width*3/4,height*5/8, paintFocus);

        //Draw midpoint
        canvas.drawCircle(width/2,height/2,width/30,paintMidpoint);
    }

    @Override
    public void update() {

    }
}
