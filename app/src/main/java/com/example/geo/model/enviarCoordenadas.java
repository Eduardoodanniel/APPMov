package com.example.geo.model;

import com.example.geo.serviceInterface.CoordenadaService;
import com.example.geo.utils.Api;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class enviarCoordenadas {


    public void eniviar(Coordenada cordenada){

        CoordenadaService coordenadaI =  Api.apiCoordenada();
        Call<ResponseBody> call = coordenadaI.enviarCoordenadas(cordenada);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                int codigoEstado = response.code();
                JSONObject respuestaJson;

                if (response.isSuccessful()){
                    try {
                        respuestaJson = new JSONObject(response.body().string());
                        System.out.println(respuestaJson.get("mensajeAplication").toString() + " code: " + codigoEstado);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        respuestaJson = new JSONObject(response.errorBody().string());
                        System.out.println(respuestaJson.get("mensajeAplication").toString() + " code: " + codigoEstado);
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



}
