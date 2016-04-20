package com.an.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.an.R;
import com.an.ViewActivity;

public class DialogInfo {
    public static TextView button;
    private static TextView tvMessage;
    private static ImageView imgView;
    public static AlertDialog dialog;


    public static void createDialogOneButton(Activity activity, String message , Bitmap img) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.alert_dialog_one_button, null);
        builder.setView(view);
        dialog = builder.create();
        tvMessage = (TextView) view.findViewById(R.id.tv_message);
        button = (TextView) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInfo.dialog.dismiss();
                ViewActivity.acceptRecord = true;
                ViewActivity.isDataAvailable = false;
                ViewActivity.viewActivity.refreshCamera();
                ViewActivity.viewActivity.record();
            }
        });
        imgView = (ImageView) view.findViewById(R.id.imgView);
        tvMessage.setText(message);
        imgView.setImageBitmap(img);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    public static void show() {
        dialog.show();
    }
}