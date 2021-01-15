package com.example.mybaseproject.utils

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(val context: Context) {
    private var PRIVATE_MODE = 0
    private val PREF_NAME = "com.vnpay.seslib"
    lateinit var editor: SharedPreferences.Editor
    lateinit var my_share: SharedPreferences

    init {
        my_share = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = my_share.edit()
    }

    fun getString(key: String): String? {
        val strStore = my_share.getString(key, "")
        if (!strStore.isNullOrEmpty()) return Utils().getE(strStore, context)
        return ""
    }


    fun setString(key: String, value: String) {
        val strValue = Utils().setE(value, context)
        editor.putString(key, strValue)
        editor.commit()
    }

    fun getList(key: String): Set<String>? {
        val strStore = my_share.getStringSet("key", null);
        if (!strStore.isNullOrEmpty()) return strStore
        return null
    }

    fun setBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.commit()
    }

    fun getBoolean(key: String): Boolean {
        return my_share.getBoolean(key, false)
    }
}