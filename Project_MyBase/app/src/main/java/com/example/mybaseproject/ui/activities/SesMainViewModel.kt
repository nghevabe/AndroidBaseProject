package com.example.mybaseproject.ui.activities

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.example.mybaseproject.data.api.ApiHelper
import com.example.mybaseproject.networks.entities.response.DataResp
import com.example.mybaseproject.ui.bases.SesBaseViewModel


class SesMainViewModel(val apiHelper: ApiHelper) : SesBaseViewModel() {

    val categoryData = MutableLiveData<DataResp>()

    fun getTest(id: Int) = launch {
        Log.d("XXX","call")
        val resp = apiHelper.testApi(id)
        categoryData.postValue(resp)
    }


}