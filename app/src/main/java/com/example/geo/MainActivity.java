package com.example.geo;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.example.geo.model.RespuestaLogin;
import com.example.geo.model.Usuario;
import com.example.geo.serviceInterface.UsuarioService;
import com.example.geo.utils.Api;
import com.google.gson.JsonObject;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.READ_PHONE_NUMBERS;
import static android.Manifest.permission.READ_PHONE_STATE;

public class MainActivity extends AppCompatActivity implements Callback<ResponseBody> {

    EditText username, password;
    TextView mensaje;
    UsuarioService usuarioServiceI;
    int PERMISO_OK = 200;
    boolean logeado = false;

    SharedPreferences sharedPreferences;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.txi_usu);
        password = (EditText) findViewById(R.id.txi_pass);
        mensaje = (TextView) findViewById(R.id.idMensaje);
        sharedPreferences = this.getSharedPreferences("Shared_pref",this.MODE_PRIVATE);
        solicitarPermiso();
        llave();
    }

    public void llave(){
        String username =sharedPreferences.getString("userName","");
        String password = sharedPreferences.getString("password","");
        long idUsuario = sharedPreferences.getLong("idUsuario", 0);

        if (!username.isEmpty() && !password.isEmpty()){
            this.username.setText(username);
            this.password.setText(password);
            if (validarPermisos()){
                logeado = true;
                Usuario user = new Usuario(username, password);
                enviarLogin(user);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void agregar(View V){
        if (validar()){
            if (validarPermisos()){
                logeado = false;
                Usuario user = new Usuario(username.getText().toString(), password.getText().toString());
                enviarLogin(user);

            }else{
                solicitarPermiso();
            }
        }
    }

    public boolean validarPermisos(){
        int resMsm = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        int resPhone1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS);
        int resPhone2 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int gps1 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        int gps2 = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if ( (resMsm == PackageManager.PERMISSION_GRANTED) || (resPhone1 == PackageManager.PERMISSION_GRANTED)  ||  (resPhone2 == PackageManager.PERMISSION_GRANTED) &&
                (  (gps1 == PackageManager.PERMISSION_GRANTED)  &&  (gps2 == PackageManager.PERMISSION_GRANTED)  ))
        {
            System.out.println("ya tiene permisos");
            return true;
        }else{
            System.out.println("no tiene permisos");
            mensaje.setText("Debes aceptar los permisos");
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void solicitarPermiso(){
        requestPermissions(new String[]{Manifest.permission.READ_SMS, Manifest.permission.READ_PHONE_NUMBERS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISO_OK);
    }


    public boolean validar (){
        boolean retorno = true;

        String c1= username.getText().toString();
        String c2= password.getText().toString();

        if (c1.isEmpty()){
            username.setError("Introduzca su usuario");
            retorno= false;
        }
        if (c2.isEmpty()){
            password.setError("Introduzca su contraseña");
            retorno= false;
        }
        return retorno;
    }

    public void enviarLogin(Usuario usuario){
        usuarioServiceI = Api.getUsuarios();
        Call<ResponseBody> call = usuarioServiceI.enviarLogin(usuario);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

        int codigoEstado = response.code();
        JSONObject respuestaJson;
        Long idUsuario = null;

        if (response.isSuccessful()){
            mensaje.setText("");
            try {
                respuestaJson = new JSONObject(response.body().string());
                idUsuario = respuestaJson.getLong("idUsuario");

                SharedPreferences.Editor editor= sharedPreferences.edit();
                editor.putLong("idUsuario", idUsuario);
                editor.putString("userName",username.getText().toString());
                editor.putString("password",password.getText().toString());
                editor.apply();
                finish();

                Intent home = new Intent(this,Home.class);
                home.putExtra("idUsuario", idUsuario);
                home.putExtra("logeado", logeado);
                startActivity(home);

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }else{
            try {
                respuestaJson = new JSONObject(response.errorBody().string());
                mensaje.setText(respuestaJson.get("mensajeAplication").toString());
            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        mensaje.setText("Error de conexion.");
        System.out.println("error: " + t.getMessage());
    }


}