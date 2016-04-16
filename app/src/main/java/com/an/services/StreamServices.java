package com.an.services;

import android.app.Service;
import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by MrAn on 15-Apr-16.
 */
public class StreamServices extends Service {

    /**
     * interface for clients that bind
     */
    public IBinder mBinder;

    /**
     * indicates whether onRebind should be used
     */
    public boolean mAllowRebind;

    /**
     * prepare value
     */
    private final String TAG = "StreamServices";
    public static final String STRING_ACTION = "STRING_ACTION";
    public static final String DATA_LABEL = "DATA";
    private Thread thread;
    private boolean isThreadRunning = false;
    public static StreamServices instances;
    private Intent intent;
    private int backCamId = 0;
    private Camera camera;

    /**
     * This services are Singleton
     *
     * @return
     */
    public StreamServices getInstance() {
        if (instances == null)
            return new StreamServices();
        return instances;
    }

    /**
     * Called when the service is being created.
     */
    @Override
    public void onCreate() {
        intent = new Intent();
    }

    /**
     * The service is starting, due to a call to startService()
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        isThreadRunning = true;
        startCamera();
        startThread();
        return START_STICKY;
    }

    private void startCamera() {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();

        Camera.getCameraInfo(getBackCameraId(), cameraInfo);
        try {
            camera = Camera.open(getBackCameraId());
            try {
                if (null == camera) {
                    Log.d(TAG, "Could not get camera instance");
                } else {
                    Log.d(TAG, "Got the camera, creating the dummy surface texture");
                    //SurfaceTexture dummySurfaceTextureF = new SurfaceTexture(0);
                    try {
                        //camera.setPreviewTexture(dummySurfaceTextureF);
                        camera.setPreviewTexture(new SurfaceTexture(0));
                        camera.startPreview();
                    } catch (Exception e) {
                        Log.d(TAG, "Could not set the surface preview texture");
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                camera.release();
            }
        } catch (RuntimeException e) {
            Log.d(TAG, "Camera not available: " + getBackCameraId());
            e.printStackTrace();
            camera = null;
        }
    }

    /**
     * Start the thread to get data
     */
    private void startThread() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isThreadRunning) {
                    try {
                        Thread.sleep(1000);
                        takePhoto();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    /**
     * Stop thread
     */
    private void stopThread() {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }

    /**
     * A client is binding to the service with bindService()
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * Called when all clients have unbound with unbindService()
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /**
     * Called when a client is binding to the service with bindService()
     */
    @Override
    public void onRebind(Intent intent) {

    }

    /**
     * Called when The service is no longer used and is being destroyed
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        isThreadRunning = false;
        stopThread();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
    }

    private int getBackCameraId(){
        if(backCamId!=0)
            return backCamId;
        else {
            int numberOfCameras = Camera.getNumberOfCameras();
            Camera.CameraInfo ci = new Camera.CameraInfo();

            for(int i = 0;i < numberOfCameras;i++){
                Camera.getCameraInfo(i,ci);
                if(ci.facing == Camera.CameraInfo.CAMERA_FACING_BACK){
                    backCamId = i;
                }
            }
            return backCamId;
        }
    }

    private void takePhoto() {
        Log.d(TAG, "Preparing to take photo");
        try {
            if (null == camera) {
                Log.d(TAG, "Could not get camera instance");
            } else {
                camera.takePicture(null, null, new Camera.PictureCallback() {
                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        intent.setAction(STRING_ACTION);
                        intent.putExtra(DATA_LABEL, data);
                        sendBroadcast(intent);
                    }
                });
            }
        } catch (Exception e) {
            camera.release();
            startCamera();
        }
    }
}
