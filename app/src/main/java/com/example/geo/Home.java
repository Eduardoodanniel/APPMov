package com.example.geo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;


public class Home extends AppCompatActivity {

    long idUsuario;
    boolean logeado;
    TextView mensaje;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mensaje = (TextView) findViewById(R.id.mensaje);

        //obtener el id del usuario
        idUsuario = getIntent().getExtras().getLong("idUsuario");
        logeado = getIntent().getExtras().getBoolean("logeado");

        //enviar datos del telefono
        if (!logeado){
            enviarDatosTelefono(getDatosTelefono());
        }

        //inicia el servicio
        startService(new Intent(this, ServiceAndroid.class));
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private Telefono getDatosTelefono()
    {
        Build build = new Build();
        Telefono telefono = new Telefono();

        telefono.setIdUsuario(idUsuario);
        telefono.setModelo(build.BRAND + " " + build.MODEL);
        telefono.setVersionAndroid(Build.VERSION.RELEASE);
        telefono.setImei(getImei());
        telefono.setNoTelefono(getNumeroTelefonico());
        telefono.setRam(getRam());
        return telefono;
    }

    private void enviarDatosTelefono(Telefono telefono){
        TelefonoService telefonoServiceI = Api.apiTelefono();
        Call<ResponseBody> call = telefonoServiceI.enviarDatosTelefono(telefono);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                int codigoEstado = response.code();
                JSONObject respuestaJson;

                if (response.isSuccessful()){
                    try {
                        respuestaJson = new JSONObject(response.body().string());
                        System.out.println(respuestaJson.get("mensajeAplication").toString() + " code: " + codigoEstado);
                        mensaje.setText(respuestaJson.get("mensajeAplication").toString());
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        respuestaJson = new JSONObject(response.errorBody().string());
                        System.out.println(respuestaJson.get("mensajeAplication").toString() + " code: " + codigoEstado);
                        mensaje.setText(respuestaJson.get("mensajeAplication").toString() + " code: " + codigoEstado);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println( "exeption: "+t.getMessage());
            }
        });
    }


    @SuppressLint("MissingPermission")
    public String getNumeroTelefonico(){
          TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        return tMgr.getLine1Number();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getImei() {
        TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(this.TELEPHONY_SERVICE);
        String stringIMEI = telephonyManager.getImei();
        return stringIMEI;
    }

    public String getRam() {
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
}
