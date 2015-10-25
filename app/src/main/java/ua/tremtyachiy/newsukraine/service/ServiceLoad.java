package ua.tremtyachiy.newsukraine.service;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import ua.tremtyachiy.newsukraine.jsonparse.JSONParse;


/*Service for load data*/
public class ServiceLoad extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        PendingIntent pi = intent.getParcelableExtra("finish");
        loadInformationFromJSON(pi);
        return super.onStartCommand(intent, flags, startId);
    }

    /*Parse JSON in another thread and send in activity when job is finish*/
    private void loadInformationFromJSON(final PendingIntent pi) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    (new JSONParse()).getInformation(getApplicationContext());
                    pi.send(ServiceLoad.this, Activity.RESULT_OK, new Intent());
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        stopSelf();
    }
}
