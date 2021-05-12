package com.example.geo.serviceInterface;


import com.example.geo.model.Telefono;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface TelefonoService {

    @PUT("save")
    Call<Map<String, Object>> enviarDatosTelefono(@Body Telefono telefono);
}
