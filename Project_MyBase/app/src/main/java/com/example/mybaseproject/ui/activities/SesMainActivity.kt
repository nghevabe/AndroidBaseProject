package com.example.mybaseproject.ui.activities

import android.content.Context
import com.example.mybaseproject.ui.bases.SesBaseActivity
import android.os.Bundle
import android.util.Log

import androidx.lifecycle.ViewModelProviders
import com.example.mybaseproject.data.BusMainRemote
import com.example.mybaseproject.data.api.ApiHelperImpl
import com.example.mybaseproject.data.remote.HttpsService
import com.example.mybaseproject.utils.ViewModelFactory
import com.example.mybaseproject.R
import com.example.mybaseproject.utils.extensions.setSafeOnClickListener
import kotlinx.android.synthetic.main.activity_main.*
import androidx.lifecycle.Observer


class SesMainActivity : SesBaseActivity() {
    override val model: SesMainViewModel
        get() = ViewModelProviders.of(this,
            ViewModelFactory(ApiHelperImpl(BusMainRemote(HttpsService.getInstance()).getService())))
            .get(SesMainViewModel::class.java)

    override val layoutId: Int = R.layout.activity_main

    override val titleId: Int =  R.string.app_name
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bt_test.setSafeOnClickListener {
            model.getTest(5)
        }
    }

    //model.categoryData
    override fun observerLiveData() {

        model.categoryData.observe(this, Observer { dataResp ->
            dataResp?.let {
                tv_test.text = it.data?.name
            }
        })

    }

}