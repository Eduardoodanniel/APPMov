package com.example.geo;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ServiceAndroid extends Service {

    MediaPlayer md;

    @Override
    public void onCreate(){
        super.onCreate();
        System.out.println("se creo el servicio");
        md = MediaPlayer.create(this,R.raw.cancion);
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int idProcess){
        md.start();
        System.out.println("inicio el servicio");
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        System.out.println("termino el servicio");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
