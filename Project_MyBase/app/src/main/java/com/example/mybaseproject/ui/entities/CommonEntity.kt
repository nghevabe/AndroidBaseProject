package com.example.mybaseproject.ui.entities

import com.google.gson.annotations.SerializedName

data class CommonEntity(@SerializedName("name") var name: String?,
                        @SerializedName("code") var code: String?) {
    @SerializedName("isCheck")
    var isCheck: Boolean = false

    @SerializedName("ivLeft")
    var ivLeft: Int = -1

    @SerializedName("value")
    var value: Int = 0

    @SerializedName("type")
    var type: String? = null

    @SerializedName("userName")
    var lastName: String? = null

    @SerializedName("birthDay")
    var birthDay: String? = null

    @SerializedName("template")
    var template: Int = -1

    @SerializedName("position")
    var position: Int = -1

    @SerializedName("paxRefIndex")
    var paxRefIndex: Int = 0

    @SerializedName("attachName")
    var attachName: String? = null

    @SerializedName("isEnable")
    var isEnable: Boolean? = false

    @SerializedName("firstName")
    var firstName: String? = null
}
