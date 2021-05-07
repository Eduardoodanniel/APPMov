package com.example.geo.utils;

import com.example.geo.serviceImplements.RetrofitService;
import com.example.geo.serviceInterface.UsuarioService;

public class Api {

    public static final String URL_01 = "http://192.168.100.214:8080/";

    public static UsuarioService getUsuarios(){
        return RetrofitService.getRetrofit(URL_01).create(UsuarioService.class);
    }
}
