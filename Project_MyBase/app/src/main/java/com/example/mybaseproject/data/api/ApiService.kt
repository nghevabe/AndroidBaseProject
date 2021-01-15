package com.example.mybaseproject.data.api

import com.example.mybaseproject.networks.entities.request.RequestBase
import com.example.mybaseproject.networks.entities.response.DataResp
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
//    @Throws(Exception::class)
//    @GET("/categories/1")
//    suspend fun testApi(@Body query: RequestBase): DataResp?

    @Throws(Exception::class)
    @GET("public-api/categories/{id}")
    suspend fun testApi(@Path("id") id: Int): DataResp?
}