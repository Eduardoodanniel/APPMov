package com.example.geo.serviceInterface;

import com.example.geo.model.Coordenada;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CoordenadaService {


    @POST("save")
    Call<Map<String, Object>> enviarCoordenadas(@Body Coordenada coordenada);


}
