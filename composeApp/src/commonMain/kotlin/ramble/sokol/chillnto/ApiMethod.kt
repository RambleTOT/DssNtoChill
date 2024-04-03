package com.example.raspberrypiserver

import org.company.app.GetData
import retrofit2.Call
import retrofit2.http.POST

interface ApiMethod {

    @POST("/")
    fun loginUser(): Call<GetData>

}