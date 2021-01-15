package com.example.mybaseproject.data

import com.example.mybaseproject.data.api.ApiService
import com.example.mybaseproject.networks.SesCommonData
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BusMainRemote(client: OkHttpClient) {
    private val service: ApiService

    init {
        val gson = GsonBuilder()
            .setLenient()
            .disableHtmlEscaping()
            .create()
        service = Retrofit.Builder()
            .baseUrl(DatasourceProperties.SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build().create(
                ApiService::class.java
            )
    }

    fun getService(): ApiService {
        return service
    }
}