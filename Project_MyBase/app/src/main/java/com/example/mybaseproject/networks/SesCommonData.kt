package com.example.mybaseproject.networks

class SesCommonData {
    var urlRoot: String? = null
    var mSession: String? = null
    var password: String? = null
    var userName: String? = null
    var hostName: String? = null
    var pinning3: String? = null
    var pinning2: String? = null
    var pinning1: String? = null
    var aesKey: String? = null
    var keyId: String? = null
    var bankCode: String? = null
    var macKey: String? = null
    var memei: String? = null
    var lan:String? = "vi"
    var phone:String? = null
    var bankToken:String? = null
    var ottToken:String? = null
    var token:String? = null

    companion object {

        private var instance: SesCommonData? = null
        fun g(): SesCommonData {
            if (instance == null)
                instance = SesCommonData()
            return instance!!
        }
    }

    fun getP1(): String {
        return pinning1!!
    }
    fun getP2(): String {
        return  pinning2!!
    }
    fun getP3(): String {
        return  pinning3!!
    }
}