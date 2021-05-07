package com.example.geo.serviceInterface;

import com.example.geo.model.Usuario;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UsuarioService {


    //Call<Usuario> getUsuario();

    @POST("login")
    Call< Map<String, Object> > enviarLogin(@Body Usuario usuario);
}
