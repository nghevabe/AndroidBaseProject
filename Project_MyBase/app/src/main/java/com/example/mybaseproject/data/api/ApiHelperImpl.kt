package com.example.mybaseproject.data.api

import com.example.mybaseproject.networks.entities.response.DataResp


class ApiHelperImpl(private val apiService: ApiService) : ApiHelper{

    override suspend fun testApi(id: Int): DataResp? {
        return apiService.testApi(id)
    }

}