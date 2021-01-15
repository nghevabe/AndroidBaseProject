package com.example.mybaseproject.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mybaseproject.data.api.ApiHelper
import com.example.mybaseproject.ui.activities.SesMainViewModel

class ViewModelFactory(private val apiHelper: ApiHelper) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(SesMainViewModel::class.java)){
            return SesMainViewModel(apiHelper) as T
        }

        throw IllegalArgumentException("Unknown class name")
    }

}