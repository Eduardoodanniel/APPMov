package com.example.geo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


import com.example.geo.model.RespuestaLogin;
import com.example.geo.model.Usuario;
import com.example.geo.serviceInterface.UsuarioService;
import com.example.geo.utils.Api;


import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<RespuestaLogin> {

    EditText username, password;
    TextView mensaje;
    UsuarioService usuarioServiceI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.txi_usu);
        password = (EditText) findViewById(R.id.txi_pass);
        mensaje = (TextView) findViewById(R.id.idMensaje);


    }

    public void enviarLogin(Usuario usuario){
        usuarioServiceI = Api.getUsuarios();
        Call<RespuestaLogin> call = usuarioServiceI.enviarLogin(usuario);
        call.enqueue(this);
    }


    public void agregar(View V){
        if (validar()){
            //Usuario user = new Usuario(username.getText().toString(), password.getText().toString());
            //enviarLogin(user);
            Intent agregar = new Intent(this,Home.class);
            startActivity(agregar);
        }
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


    @Override
    public void onResponse(Call<RespuestaLogin> call, Response<RespuestaLogin> response) {
        if (response.isSuccessful()){
            RespuestaLogin resLogin = response.body();
            if (resLogin.getIdUsuario() != null){
                mensaje.setText("Correcto");
                Intent agregar = new Intent(this,Home.class);
                startActivity(agregar);
                return;
            }
            mensaje.setText(resLogin.getMensajeAplication());
        }else{
            mensaje.setText("OCURRIO UN ERROR DE MAPEO");
        }
    }

    @Override
    public void onFailure(Call<RespuestaLogin> call, Throwable t) {
        mensaje.setText("Error de conexion.");
        System.out.println("error: " + t.getMessage());
    }


}

