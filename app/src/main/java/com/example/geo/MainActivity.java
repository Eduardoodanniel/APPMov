package com.example.geo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


import com.example.geo.model.Usuario;
import com.example.geo.serviceInterface.UsuarioService;
import com.example.geo.utils.Api;


import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<Map<String, Object>> {

    EditText username, password;
    UsuarioService usuarioServiceI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.txi_usu);
        password = (EditText) findViewById(R.id.txi_pass);



    }

    public void enviarLogin(Usuario usuario){

        usuarioServiceI = Api.getUsuarios();
        Call<Map<String, Object>> call = usuarioServiceI.enviarLogin(usuario);
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
    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
        if (response.isSuccessful()){
            System.out.println(response.body());
            return;
        }
        System.out.println(" no se parseo body");
    }

    @Override
    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
        System.out.println(t.getMessage());
    }


}

