package com.example.mybaseproject.data.api
import com.example.mybaseproject.networks.entities.response.DataResp

interface ApiHelper {
    suspend fun testApi(id: Int): DataResp?
}