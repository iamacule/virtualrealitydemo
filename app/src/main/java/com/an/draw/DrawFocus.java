package com.an.draw;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.AsyncTask;
import android.util.Log;

import com.an.ViewActivity;
import com.an.util.DataUtil;

/**
 * Created by MrAn on 19-Apr-16.
 */
public class DrawFocus implements Draw {
    private Activity activity;
    private Paint paintFocus;
    private Paint paintMidpoint;
    private int width;
    private int height;
    private Point point;
    private ProgressDialog progressDialog;

    public DrawFocus(Activity activity) {
        this.activity = activity;
        progressDialog = new ProgressDialog(activity);
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
        setListRange();
    }

    private void setListRange() {
        new SetListRangeAsync().execute(new Void[]{});
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

    private class SetListRangeAsync extends AsyncTask<Void,Void,Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if(DataUtil.listRangeFocus.size()<=0){
                for (int i = width/4 ; i<width*3/4 ; i++){
                    for (int j = height*3/8 ; j<height*5/8 ; j++){
                        point = new Point(i,j);
                        DataUtil.listRangeFocus.add(point);
                        Log.d("Add range data",""+point.x + " : "+point.y);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(progressDialog.isShowing())
                progressDialog.dismiss();
            ViewActivity.acceptRecord = true;
            ViewActivity.viewActivity.record();
        }
    }
}
