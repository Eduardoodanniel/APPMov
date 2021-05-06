package com.example.geo;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class ServiceAndroid extends Service {

    MediaPlayer md;

    @Override
    public void onCreate(){
        super.onCreate();
        md = MediaPlayer.create(this,R.raw.cancion);
        md.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int idProcess){
        md.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy(){

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
