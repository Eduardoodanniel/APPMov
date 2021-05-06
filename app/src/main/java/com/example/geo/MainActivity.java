package com.example.geo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText usuario,contraseña;
    Button iniciar_sesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usuario = (EditText) findViewById(R.id.txi_usu);
        contraseña = (EditText) findViewById(R.id.txi_pass);
    }

    public void agregar(View V){

        if (validar()){
            Toast.makeText(this, "Ingreso datos", Toast.LENGTH_SHORT).show();
            Intent agregar = new Intent(this,Home.class);
            startActivity(agregar);

        }
    }


    public boolean validar (){

        boolean retorno = true;

        String c1= usuario.getText().toString();
        String c2= contraseña.getText().toString();

        if (c1.isEmpty()){
            usuario.setError("Introduzca su usuario");
            retorno= false;
        }
        if (c2.isEmpty()){
            contraseña.setError("Introduzca su contraseña");
            retorno= false;
        }

        return true;

    }




}