package com.example.mybaseproject.networks.entities.request

import android.content.Context
import com.example.mybaseproject.di.Common
import com.example.mybaseproject.utils.Utils
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

open class RequestBase(activity: Context) {

    @SerializedName("phone")
    @Expose(deserialize = false, serialize = true)
    var phone: String? = null

    @SerializedName("version")
    @Expose(deserialize = false, serialize = true)
    var version: String? = null

    @SerializedName("deviceType")
    @Expose(deserialize = false, serialize = true)
    var deviceType: String? = null

    @SerializedName("deviceId")
    @Expose(deserialize = false, serialize = true)
    var deviceId: String? = null

    @SerializedName("nonce")
    @Expose(deserialize = false, serialize = true)
    var nonce: Long = 0

    @SerializedName("deviceModel")
    @Expose(deserialize = false, serialize = true)
    var deviceModel: String? = "Android"

    @SerializedName("screenSize")
    @Expose(deserialize = false, serialize = true)
    var screenSize: String? = null

    @SerializedName("sdkVersion")
    @Expose(deserialize = false, serialize = true)
    var sdkVersion: String? = null

    @SerializedName("mid")
    @Expose(deserialize = false, serialize = true)
    var mid: String? = null

    @SerializedName("data")
    @Expose(deserialize = false, serialize = true)
    var data: Any? = null

    @SerializedName("bankCode")
    @Expose(deserialize = false, serialize = true)
    var bankCode: String? = null

    @SerializedName("bankToken")
    @Expose(deserialize = false, serialize = true)
    var bankToken: String? = null

    fun setData(value: Any): RequestBase {
        data = value
        return this
    }

    init {
        this.deviceId = Utils().getImei(activity)

        this.deviceType = "1"
        val calendar = Calendar.getInstance()
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        this.nonce = System.currentTimeMillis()
        this.screenSize = Utils()
            .getDeviceSizes(Common.baseActivity, Utils().SCREEN_WIDTH)
            .toString() + "*" + Utils()
            .getDeviceSizes(Common.baseActivity, Utils().SCREEN_HEIGHT)
            .toString()
        this.sdkVersion = "1.0"
        this.mid = mid
    }
}