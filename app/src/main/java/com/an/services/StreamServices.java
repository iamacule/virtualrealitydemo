package com.an.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by MrAn on 15-Apr-16.
 */
public class StreamServices extends Service {

    /** interface for clients that bind */
    public IBinder mBinder;

    /** indicates whether onRebind should be used */
    public boolean mAllowRebind;

    /** prepare value*/
    private final String TAG = "StreamServices";
    public static final String STRING_ACTION = "STRING_ACTION";
    public static final String DATA_LABEL = "DATA";
    private Thread thread;
    private boolean isThreadRunning = false;
    public static StreamServices instances;
    private Intent intent;

    /**
     * This services are Singleton
     * @return
     */
    public StreamServices getInstance(){
        if(instances==null)
            return new StreamServices();
        return instances;
    }

    /** Called when the service is being created. */
    @Override
    public void onCreate() {
    }

    /** The service is starting, due to a call to startService() */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        isThreadRunning = true;
        startThread();
        return START_STICKY;
    }

    /**
     * Start the thread to get data
     */
    private void startThread() {
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isThreadRunning){
                    try{
                        Thread.sleep(2000);
                        intent = new Intent();
                        intent.setAction(STRING_ACTION);
                        int range = (1000 - 0) + 1;
                        intent.putExtra(DATA_LABEL,""+(Math.random()*range)+0);
                        sendBroadcast(intent);
                    }catch (Exception e){
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
    private void stopThread(){
        if(thread!=null&&thread.isAlive()){
            thread.interrupt();
        }
    }

    /** A client is binding to the service with bindService() */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** Called when all clients have unbound with unbindService() */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /** Called when a client is binding to the service with bindService()*/
    @Override
    public void onRebind(Intent intent) {

    }

    /** Called when The service is no longer used and is being destroyed */
    @Override
    public void onDestroy() {
        super.onDestroy();
        isThreadRunning = false;
        stopThread();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_SHORT).show();
    }
}
