package com.example.geo;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.geo.model.Coordenada;
import com.example.geo.model.enviarCoordenadas;

public class ServiceAndroid extends Service implements LocationListener {


    LocationManager locationManager;
    Location location;
    Coordenada coordenada;
    enviarCoordenadas enviarCoor;

    public boolean gpsIsActivo(){
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @SuppressLint("MissingPermission")
    public void iniciarLocation() {
        try {
            locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        } catch (Exception e) {
            System.out.println("error al definir la variable locationManager");
            System.out.println("exception: " + e.getMessage());
        }

        if (gpsIsActivo())
        {
            try {
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 29*60000, 0, this);
                location = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
            } catch (Exception e) {
                System.out.println("error al definir la variable location");
                System.out.println("exception: " + e.getMessage());
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate(){
        super.onCreate();
        coordenada  = new Coordenada();
        enviarCoor = new enviarCoordenadas();
        coordenada.setImei(getImei());
        iniciarLocation();
        System.out.println("se creo el servicio");
    }

    @Override
    public int onStartCommand(Intent intent, int flag, int idProcess){
        System.out.println("inicio el servicio");
        Context ct = this;
        final Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (gpsIsActivo()){

                    if (coordenada.getLatitud() == 0){
                        iniciarLocation();
                    }
                    coordenada.setBateria(getBateria());
                    enviarCoor.eniviar(coordenada);
                }else{
                    Toast.makeText(ct, "Avtiva tu GPS", Toast.LENGTH_SHORT).show();
                }
                handler.postDelayed(this,30*60000);//se ejecutara cada 30 minutos
            }
        }, 40000);//empezara a ejecutarse después 40 segundos
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
        coordenada.setLatitud(location.getLatitude());
        coordenada.setLongitud(location.getLongitude());
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        System.out.println("se Activo el gps: " + provider);
        iniciarLocation();
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        System.out.println("se desactivo el gps: " + provider);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getImei() {
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
        String stringIMEI = telephonyManager.getImei();
        return stringIMEI;
    }

    public String getBateria(){
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        double battery = (level / (double)scale)*100;
        String res = String.valueOf(battery).substring(0, 4);
        return res.concat("%");
    }

}
