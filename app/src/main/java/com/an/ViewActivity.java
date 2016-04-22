package com.an;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.an.draw.DrawMain;
import com.an.model.ViewModel;
import com.an.util.DataUtil;
import com.an.util.ResizeBitmap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrAn on 15-Apr-16.
 */
public class ViewActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    private SurfaceView surfaceView;
    private LinearLayout lnDraw;
    private DrawMain drawMain;
    private Button btnBack;
    private Thread threadRecord;
    private Bitmap bpData;
    public static boolean acceptRecord = true;
    public static ViewActivity viewActivity;
    public static boolean isDataAvailable = false;
    private final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private final int MY_PERMISSIONS_REQUEST_STORAGE = 2;
    private SurfaceHolder surfaceHolder;
    private Camera.PictureCallback jpegCallback;
    private Camera camera;
    private final String TAG = "ViewActivity";
    private Matrix matrix;
    private List<ViewModel> listModel;
    public static Bitmap defaultModel;
    public static Bitmap transparentModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        viewActivity = this;
        checkPermission(Manifest.permission.CAMERA, MY_PERMISSIONS_REQUEST_CAMERA);
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, MY_PERMISSIONS_REQUEST_STORAGE);
        initParameter();
        addPictureCallBack();
        setOnClick();
        record();
    }


    /**
     * Start a thread to record and get bitmap from camera
     */
    public void record() {
        threadRecord = new Thread(new Runnable() {
            @Override
            public void run() {
                while (acceptRecord) {
                    try {
                        Thread.sleep(2000);
                        Log.d(TAG, "Recording");
                        captureImage();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        threadRecord.start();
        Log.d(TAG, "Record started");
    }

    private void setOnClick() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * Get bitmap from camera
     */
    private void addPictureCallBack() {
        jpegCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                bpData = BitmapFactory.decodeByteArray(data, 0, data.length);
                if (bpData != null && !isDataAvailable) {

                    //True size from camera
                    bpData = rotateBitmap(bpData);

                    //Resize to show in the dialog
//                    bpData = ResizeBitmap.resize(bpData, DataUtil.screenWidth / 4);

                    /*
                        Crop bitmap at QRCode area for detect
                     */
                    cropBitmap(bpData);
                    bpData = Bitmap.createBitmap(bpData, DataUtil.cropX, DataUtil.cropY, DataUtil.cropWidth, DataUtil.cropHeight);
                    pickerBitmap(bpData);
                    //Refresh camera after get bitmap
                    refreshCamera();

                    //Preview bitmap
//                    acceptRecord = false;
//                    isDataAvailable = true;
//                    DialogInfo.createDialogOneButton(viewActivity, "Image", bpData);
//                    DialogInfo.show();
                }
            }
        };
    }

    private void pickerBitmap(Bitmap bitmap) {
        int y = bitmap.getHeight()/2;
        Point p1 = new Point();
        for (int x=0;x<bitmap.getWidth()/4;x++){
            int colour = bitmap.getPixel(x,y);
            int red = Color.red(colour);
            int blue = Color.blue(colour);
            int green = Color.green(colour);
            if(red<50&&blue<50&&green<50){
                p1.set(x,y);
                break;
            }
        }
        StringBuilder pickerId = new StringBuilder();
        boolean lookingForWhite = true;
        for (int x=p1.x;x<bitmap.getWidth();x++){
            int colour = bitmap.getPixel(x,y);
            int red = Color.red(colour);
            int blue = Color.blue(colour);
            int green = Color.green(colour);
            if (lookingForWhite){
                if(red>50&&blue>50&&green>50){
                    pickerId.append(DataUtil.WHITE);
                    lookingForWhite = false;
                }
            }else {
                if(red<50&&blue<50&&green<50){
                    pickerId.append(DataUtil.BLACK);
                    lookingForWhite = true;
                }
            }
        }

        if(pickerId.length()>=3){
            String sub = pickerId.substring(0,pickerId.length()-2);
            for (ViewModel viewModel : listModel){
                if(viewModel.getId().equals(sub)){
                    defaultModel = viewModel.getBpView();
                    Log.e(TAG,"Picker Name : "+viewModel.getModelName());
                    break;
                }
            }
        } else {
            defaultModel = transparentModel;
        }
    }

    /**
     * Crop bitmap
     */
    private void cropBitmap(Bitmap bitmap) {
        if (DataUtil.cropX <= 0) {
            DataUtil.cropX = (bitmap.getWidth() / 4) + (bitmap.getWidth() / 50);
            DataUtil.cropY = (bitmap.getHeight() * 3 / 8) + (bitmap.getWidth() / 50);
            DataUtil.cropWidth = (bitmap.getWidth() / 8);
            DataUtil.cropHeight = (bitmap.getWidth() / 8);
        }
    }

    /**
     * Init value from begin
     */
    private void initParameter() {
        viewActivity = this;
        matrix = new Matrix();
        matrix.postRotate(90);
        transparentModel = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
        transparentModel.eraseColor(Color.TRANSPARENT);
        defaultModel = transparentModel;
        surfaceView = (SurfaceView) findViewById(R.id.surStream);
        lnDraw = (LinearLayout) findViewById(R.id.lnDraw);
        drawMain = new DrawMain(getApplicationContext(), this);
        lnDraw.addView(drawMain);
        btnBack = (Button) findViewById(R.id.btnBack);
        surfaceHolder = surfaceView.getHolder();
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        surfaceHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        createListModel();
    }

    private void createListModel() {
        listModel = new ArrayList<>();

        ViewModel covisoft = new ViewModel();
        covisoft.setBpView(BitmapFactory.decodeResource(getResources(),R.mipmap.covisoft_logo));
        DataUtil.stringTemp = new StringBuffer();
        DataUtil.stringTemp.append(DataUtil.WHITE);
        DataUtil.stringTemp.append(DataUtil.BLACK);
        DataUtil.stringTemp.append(DataUtil.WHITE);
        DataUtil.stringTemp.append(DataUtil.BLACK);
        DataUtil.stringTemp.append(DataUtil.WHITE);
        covisoft.setId(DataUtil.stringTemp.toString());
        covisoft.setModelName("Covisoft");

        ViewModel barcelona = new ViewModel();
        barcelona.setBpView(ResizeBitmap.resize(BitmapFactory.decodeResource(getResources(),R.mipmap.barcelona),
                                DataUtil.screenWidth/2));
        DataUtil.stringTemp = new StringBuffer();
        DataUtil.stringTemp.append(DataUtil.WHITE);
        DataUtil.stringTemp.append(DataUtil.BLACK);
        DataUtil.stringTemp.append(DataUtil.WHITE);
        DataUtil.stringTemp.append(DataUtil.BLACK);
        DataUtil.stringTemp.append(DataUtil.WHITE);
        DataUtil.stringTemp.append(DataUtil.BLACK);
        DataUtil.stringTemp.append(DataUtil.WHITE);
        barcelona.setId(DataUtil.stringTemp.toString());
        barcelona.setModelName("Barcelona");

        ViewModel chelsea = new ViewModel();
        chelsea.setBpView(ResizeBitmap.resize(BitmapFactory.decodeResource(getResources(),R.mipmap.chelsea),
                DataUtil.screenWidth/2));
        DataUtil.stringTemp = new StringBuffer();
        DataUtil.stringTemp.append(DataUtil.WHITE);
        DataUtil.stringTemp.append(DataUtil.BLACK);
        DataUtil.stringTemp.append(DataUtil.WHITE);
        DataUtil.stringTemp.append(DataUtil.BLACK);
        DataUtil.stringTemp.append(DataUtil.WHITE);
        DataUtil.stringTemp.append(DataUtil.BLACK);
        DataUtil.stringTemp.append(DataUtil.WHITE);
        DataUtil.stringTemp.append(DataUtil.BLACK);
        DataUtil.stringTemp.append(DataUtil.WHITE);
        DataUtil.stringTemp.append(DataUtil.BLACK);
        DataUtil.stringTemp.append(DataUtil.WHITE);
        chelsea.setId(DataUtil.stringTemp.toString());
        chelsea.setModelName("Chelsea");

        ViewModel mu = new ViewModel();
        mu.setBpView(ResizeBitmap.resize(BitmapFactory.decodeResource(getResources(),R.mipmap.mu),
                DataUtil.screenWidth/2));
        DataUtil.stringTemp = new StringBuffer();
        DataUtil.stringTemp.append(DataUtil.WHITE);
        DataUtil.stringTemp.append(DataUtil.BLACK);
        DataUtil.stringTemp.append(DataUtil.WHITE);
        DataUtil.stringTemp.append(DataUtil.BLACK);
        DataUtil.stringTemp.append(DataUtil.WHITE);
        DataUtil.stringTemp.append(DataUtil.BLACK);
        DataUtil.stringTemp.append(DataUtil.WHITE);
        DataUtil.stringTemp.append(DataUtil.BLACK);
        DataUtil.stringTemp.append(DataUtil.WHITE);
        mu.setId(DataUtil.stringTemp.toString());
        mu.setModelName("MU");

        ViewModel real = new ViewModel();
        real.setBpView(ResizeBitmap.resize(BitmapFactory.decodeResource(getResources(),R.mipmap.real),
                DataUtil.screenWidth/2));
        DataUtil.stringTemp = new StringBuffer();
        DataUtil.stringTemp.append(DataUtil.WHITE);
        DataUtil.stringTemp.append(DataUtil.BLACK);
        DataUtil.stringTemp.append(DataUtil.WHITE);
        real.setId(DataUtil.stringTemp.toString());
        real.setModelName("Real");

        listModel.add(covisoft);
        listModel.add(barcelona);
        listModel.add(chelsea);
        listModel.add(mu);
        listModel.add(real);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRecord();
    }

    /**
     * Stop thread record
     */
    private void stopRecord() {
        if (threadRecord != null && threadRecord.isAlive()) {
            threadRecord.interrupt();
            threadRecord = null;
        }
        acceptRecord = false;
        Log.d(TAG, "Record stopped");
    }

    /**
     * Rotate bitmap with 90 degree
     *
     * @param source
     * @return
     */
    public Bitmap rotateBitmap(Bitmap source) {
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    /**
     * Capture image
     *
     * @throws IOException
     */
    public void captureImage() throws IOException {
        camera.takePicture(null, null, jpegCallback);
    }

    /**
     * Refresh camera when capture image success
     */
    public void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
        }
    }


    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // Now that the size is known, set up the camera parameters and begin
        // the preview.
        refreshCamera();
        camera.setDisplayOrientation(getRotationAngle());
    }

    /**
     * Get an rotation angle
     *
     * @return
     */
    public int getRotationAngle() {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info);
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // open the camera
            camera = Camera.open();
        } catch (RuntimeException e) {
            // check for exceptions
            return;
        }
        Camera.Parameters param;
        param = camera.getParameters();
        // modify parameter
        param.setPreviewSize(352, 288);
        camera.setParameters(param);
        try {
            // The Surface has been created, now tell the camera where to draw
            // the preview.
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            // check for exceptions
            return;
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // stop preview and release camera
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    /**
     * Checking permission working with device over API 23
     *
     * @param permission
     * @param idCallback
     */
    private void checkPermission(String permission, int idCallback) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{permission},
                        idCallback);

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

            case MY_PERMISSIONS_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(viewActivity, "PermissionSTORAGE granted !", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }
}
