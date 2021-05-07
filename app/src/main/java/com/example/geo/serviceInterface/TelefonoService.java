package com.example.geo.serviceInterface;


import com.example.geo.model.Telefono;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface TelefonoService {

    @PUT("save")
    Call<Boolean> enviarDatosTelefono(@Body Telefono telefono);
}
