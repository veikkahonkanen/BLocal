package com.bteam.blocal.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.bteam.blocal.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ForegroundService extends Service {

    private static final String TAG = "ForegroundService";
    private static final String SERVICE_CHANNEL = "serviceChannel";
    private static final int NOTIFICATION_ID = 42;
    private ExecutorService execService;
    private int i;
    private int sleepTime;
    private boolean started;
    private Future<?> currentTask;


    public ForegroundService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
        i = 0;
        sleepTime = 1000;
        Log.d(TAG, "onCreate: Service created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null && !started){
            if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                NotificationChannel channel = new NotificationChannel(SERVICE_CHANNEL, "Foreground Service", NotificationManager.IMPORTANCE_LOW);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(channel);
            }
            Notification notification = new NotificationCompat.Builder(this, SERVICE_CHANNEL)
                    .setContentTitle(this.getResources().getString(R.string.blocal_is_running))
                    //.setContentText("")
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    //.setTicker("")
                    .build();

            startForeground(NOTIFICATION_ID, notification);
            doWork();
            started = true;
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        started = false;
        if(currentTask != null){
            currentTask.cancel(true);
        }
        super.onDestroy();
    }


    private void doWork(){
        if(execService == null){
            execService = Executors.newSingleThreadExecutor();
        }

        if(currentTask != null && currentTask.isCancelled()){
            return;
        }
        currentTask = execService.submit(() -> {
            while(started) {
                i++;
                Log.d(TAG, "Work done: " + i);
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    Log.e(TAG, "ERROR: ", e);
                    return;
                }
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
