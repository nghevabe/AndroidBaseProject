package com.example.mybaseproject.networks.entities.restful
import com.google.gson.annotations.SerializedName

/**
 * Created by Lvhieu2016 on 10/3/2016.
 */

abstract class ResponseEntity<T> {
    @SerializedName("mid")
    val mid: Int = 0

    @SerializedName("code")
    val code: String? = null

    @SerializedName("desc")
    val desc: String? = null

    @SerializedName("data")
    val data: T? = null

    @SerializedName("source")
    var source: String? = null

    @SerializedName("retryPayment") val retryPayment:String? = null

    fun isSuccess() : Boolean{
        return "00".equals(code)
    }
}

