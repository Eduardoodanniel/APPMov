package com.example.geo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.BatteryManager;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.geo.model.Coordenada;
import com.example.geo.model.enviarCoordenadas;

public class ServiceAndroid extends Service implements LocationListener {

    double latitud, longitud;
    LocationManager locationManager;
    Location location;
    boolean gpsActivo;
    Coordenada coordenada;
    enviarCoordenadas enviarCoor;


    @SuppressLint("MissingPermission")
    public void getLocation() {
        try {
            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
            gpsActivo = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
        }

        if (gpsActivo) {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 1000, 0, this);
            location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            latitud = location.getLatitude();
            longitud = location.getLongitude();
        }
    }

    @Override
    public void onCreate(){
        super.onCreate();
        getLocation();
        coordenada  = new Coordenada();
        enviarCoor = new enviarCoordenadas();
        System.out.println("se creo el servicio");
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int idProcess){
        System.out.println("inicio el servicio");
            final Handler handler= new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    coordenada.setLatitud(latitud);
                    coordenada.setLongitud(longitud);
                    coordenada.setBateria(getBateria());
                    enviarCoor.eniviar(coordenada);
                    handler.postDelayed(this,10000);//se ejecutara cada 2 segundos
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


    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitud = location.getLatitude();
        longitud = location.getLongitude();
    }


    public String getBateria(){
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        double battery = (level / (double)scale)*100;
        String res = String.valueOf(battery);
        return res.concat("%");
    }

}
