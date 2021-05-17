package com.example.geo.serviceInterface;

import com.example.geo.model.Coordenada;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface CoordenadaService {


    @POST("save")
    Call<ResponseBody> enviarCoordenadas(@Body Coordenada coordenada);


}
