package com.example.geo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import com.example.geo.model.Telefono;
import com.example.geo.serviceInterface.TelefonoService;
import com.example.geo.utils.Api;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;


public class Home extends AppCompatActivity {

    private static final String READ_SMS = "";
    Telefono telefono = new Telefono();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getNumeroTelefonico();

        //envia datos del telefono
        enviarDatosTelefono();

        //inicia el servicio
        //startService(new Intent(this, ServiceAndroid.class));
    }

    TelefonoService telefonoServiceI;

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void guardarDatosTelefono()
    {
        Build build = new Build();
        telefono.setModelo(build.BRAND + " " + build.MODEL);
        telefono.setIdUsuario(1L);
        telefono.setImei(GetIMEI());
        //bateria
        //numero de telefono

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void enviarDatosTelefono(){
        guardarDatosTelefono();
        telefonoServiceI = Api.apiTelefono();
        Call<Boolean> call = telefonoServiceI.enviarDatosTelefono(telefono);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response != null){
                    if (response.code() == 201){
                        System.out.println("####################################");
                        System.out.println("respusta correcta");
                        System.out.println("####################################");
                    }else{
                        System.out.println("####################################");
                        System.out.println("estado: " + response.code());
                        System.out.println("####################################");
                    }
                }else{
                    System.out.println("####################################");
                    System.out.println("Ocurrio un error");
                    System.out.println("####################################");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                System.out.println("####################################");
                System.out.println( "exeption: "+t.getMessage());
                System.out.println("####################################");
            }
        });
    }








    @RequiresApi(api = Build.VERSION_CODES.M)
    public void getNumeroTelefonico(){

        if (ActivityCompat.checkSelfPermission(this, READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, READ_PHONE_NUMBERS) ==
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tMgr = (TelephonyManager)   this.getSystemService(Context.TELEPHONY_SERVICE);
            String mPhoneNumber = tMgr.getLine1Number();
            textView.setText(mPhoneNumber);
            return;
        } else {
            requestPermission();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{READ_SMS, READ_PHONE_NUMBERS, READ_PHONE_STATE}, 100);
        }
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                TelephonyManager tMgr = (TelephonyManager)  this.getSystemService(Context.TELEPHONY_SERVICE);
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED  &&
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) !=      PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                String mPhoneNumber = tMgr.getLine1Number();
                System.out.println("numero telefonico:  " + mPhoneNumber);
                //telefono.setNoTelefono(mPhoneNumber);
                break;
        }
    }












    @RequiresApi(api = Build.VERSION_CODES.O)
    public String GetIMEI() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PackageManager.PERMISSION_GRANTED);
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "no se aceptaron permisos." ;
        }
        String stringIMEI = telephonyManager.getImei();
        return stringIMEI;
    }


    public double getBateria(){
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        double battery = (level / (double)scale)*100;
        return battery;
    }


    //debe retornar un double
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

    private LocationManager ubicacion;
    TextView longitud, latitud, textView;

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
