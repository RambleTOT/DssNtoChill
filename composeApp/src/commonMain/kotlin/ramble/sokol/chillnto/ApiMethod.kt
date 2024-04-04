package com.example.raspberrypiserver

import org.company.app.GetData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiMethod {

    @GET("/")
    fun loginUser(): Call<GetData>

}