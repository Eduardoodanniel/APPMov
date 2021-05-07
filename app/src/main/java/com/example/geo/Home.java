package com.example.geo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //inicia el servicio
        startService(new Intent(this, ServiceAndroid.class));

        /*
        localizacion();
        listaProvider();
        mejorCriterio();
        estadoGPS();
        registrarLocalizacion();
        deviceInfo();
        getMemory();
        */
    }




















    private LocationManager ubicacion;
    TextView longitud, latitud, textView;
    Build build;
    String information;


    private void deviceInfo() {
        information = "PRODUCTO: " + build.PRODUCT + "\n" +
                "BRAND: " + build.BRAND + "\n" +
                "HARDWARE: " + build.HARDWARE + "\n" +
                "DISPOSITIVO: " + build.DEVICE + "\n" +
                "MODELO: " + build.MODEL + "\n" +
                "HARDWARE: " + build.HARDWARE + "\n";

    }

    public String getMemory() {

        RandomAccessFile reader;
        String load;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam;
        String lastValue = "";

        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
            }

            reader.close();

            totRam = Double.parseDouble(value);

            double mb = totRam / 1024.0;
            double gb = totRam / 1048576.0;
            double tb = totRam / 1073741824.0;

            if (tb > 1) {
                lastValue = twoDecimalForm.format(tb).concat(" TB");
            } else if (gb > 1) {
                lastValue = twoDecimalForm.format(gb).concat(" GB");
            } else if (mb > 1) {
                lastValue = twoDecimalForm.format(mb).concat(" MB");
            } else {
                lastValue = twoDecimalForm.format(totRam).concat(" KB");
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return lastValue;
    }

    private void localizacion() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1000);
        }
        longitud = (TextView) findViewById(R.id.txtLongitud);
        latitud = (TextView) findViewById(R.id.txtLatitud);
        ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location loc = ubicacion.getLastKnownLocation(LocationManager.GPS_PROVIDER);
          if (ubicacion != null) {
              System.out.println("Se requiere acceso a tu ubicacion");

               // Log.d("Latitud", String.valueOf(loc.getLatitude()));
                // Log.d("Longitud", String.valueOf(loc.getLongitude()));
            }
    }

    private void listaProvider() {
        ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> listaProvider = ubicacion.getAllProviders();

        String mejorProvider = ubicacion.getBestProvider(mejorCriterio(), false);
        System.out.println(mejorProvider);

        LocationProvider provider = ubicacion.getProvider(listaProvider.get(0));
        System.out.println(provider.getAccuracy());
        System.out.println(provider.getPowerRequirement());
        System.out.println(provider.supportsAltitude());
    }

    private Criteria mejorCriterio() {
        Criteria requerimiento = new Criteria();
        requerimiento.setAccuracy(Criteria.ACCURACY_FINE);
        requerimiento.setAltitudeRequired(true);
        return requerimiento;
    }

    private boolean estadoGPS() {
        ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!ubicacion.isProviderEnabled(LocationManager.GPS_PROVIDER))
            Log.d("GPS", "NO ACTIVADO");
        else {
            Log.d("GPS", "ACTIVADO");
        }
        return true;
    }

    private void registrarLocalizacion(){
        ubicacion = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        ubicacion.requestLocationUpdates(LocationManager.GPS_PROVIDER, 900000, 0, new milocalizacionListener());
    }

    private class milocalizacionListener implements LocationListener {


        @Override
        public void onLocationChanged(Location location) {
            String lat = "Mi latitud es: " + location.getLatitude();
            String lon = "Mi longitud es: " + location.getLongitude();
            latitud.setText(lat);
            longitud.setText(lon);
            System.out.println(lat);
            System.out.println(lon);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled( String provider) {

        }

        @Override
        public void onProviderDisabled( String provider) {

        }
    }
}
