package com.example.geo.serviceInterface;

import com.example.geo.model.RespuestaLogin;
import com.example.geo.model.Usuario;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UsuarioService {

    @POST("login")
    Call<RespuestaLogin> enviarLogin(@Body Usuario usuario);
}
