package com.example.geo.model;

import com.example.geo.serviceInterface.CoordenadaService;
import com.example.geo.utils.Api;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class enviarCoordenadas {


    public void eniviar(Coordenada cordenada){

        CoordenadaService coordenadaI =  Api.apiCoordenada();
        Call<Map<String, Object>> call = coordenadaI.enviarCoordenadas(cordenada);
        call.enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response != null){
                    if (response.code() == 201){
                        System.out.println("respusta correcta");
                    }else{
                        System.out.println("estado: " + response.code());
                    }
                }else{
                    System.out.println("Ocurrio un error");
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                System.out.println( "exeption: "+t.getMessage());
            }
        });

    }



}
