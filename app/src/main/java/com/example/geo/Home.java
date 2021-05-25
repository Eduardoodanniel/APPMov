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
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import com.example.geo.model.Telefono;
import com.example.geo.serviceInterface.TelefonoService;
import com.example.geo.utils.Api;
import com.example.geo.utils.Imei;

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
        if (!logeado) {
            enviarDatosTelefono(getDatosTelefono());
        }

        //inicia el servicio
        startService(new Intent(this, ServiceAndroid.class));
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private Telefono getDatosTelefono() {
        Build build = new Build();
        Telefono telefono = new Telefono();

        telefono.setIdUsuario(idUsuario);
        telefono.setModelo(build.BRAND + " " + build.MODEL);
        telefono.setVersionAndroid(Build.VERSION.RELEASE);
        telefono.setImei(Imei.getIMEIDeviceId(this));
        telefono.setNoTelefono(getNumeroTelefonico());
        telefono.setRam(getRam());
        telefono.setAlmacenamientoTotal(String.valueOf(getMemoriaTotal()));
        return telefono;
    }

    private void enviarDatosTelefono(Telefono telefono) {

        TelefonoService telefonoServiceI = Api.apiTelefono();
        Call<ResponseBody> call = telefonoServiceI.enviarDatosTelefono(telefono);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                int codigoEstado = response.code();
                JSONObject respuestaJson;

                if (response.isSuccessful()) {
                    try {
                        respuestaJson = new JSONObject(response.body().string());
                        System.out.println(respuestaJson.get("mensajeAplication").toString() + " code: " + codigoEstado);
                        mensaje.setText(respuestaJson.get("mensajeAplication").toString());
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
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
                System.out.println("exeption: " + t.getMessage());
            }
        });
    }


    //@SuppressLint("MissingPermission")
    public String getNumeroTelefonico() {
        TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        return tMgr.getLine1Number();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public Long getMemoriaTotal() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
         //StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSizeLong();
        long totalBlocks = stat.getBlockCountLong();
        return (totalBlocks * blockSize / (1024*1024));
    }

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
                if(size >= 1024);
                suffix = "GB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
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
