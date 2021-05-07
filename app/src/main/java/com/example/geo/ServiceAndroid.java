package com.example.geo;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class ServiceAndroid extends Service {

    @Override
    public void onCreate(){
        super.onCreate();
        System.out.println("se creo el servicio");
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int idProcess){
        System.out.println("inicio el servicio");


            final Handler handler= new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    System.out.println("imprimiendo");
                    handler.postDelayed(this,3000);//se ejecutara cada 2 segundos
                }
            },5000);//empezara a ejecutarse después de 5 segundos




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
