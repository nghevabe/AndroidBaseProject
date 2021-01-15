package com.example.mybaseproject.data.remote

import android.content.Context
import android.util.Base64
import com.example.mybaseproject.data.BusMainRemote
import com.example.mybaseproject.data.api.ApiService
import com.example.mybaseproject.di.Common
import com.example.mybaseproject.networks.SesCommonData

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.CertificatePinner
import okhttp3.MediaType
import okhttp3.OkHttpClient
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.Certificate
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

object HttpsService {
    fun getInstance() : OkHttpClient{
        val okHttp = createOkHttpClient(createOkHttpCache(Common.baseActivity))

        return newBuildOkHttp(okHttp, DatasourceProperties.TIMEOUT_CONNECT, DatasourceProperties.TIMEOUT_READ)
    }

    fun createOkHttpClient(
        cache: Cache
    ): OkHttpClient {

        return OkHttpClient.Builder()
            .cache(cache)
//            .hostnameVerifier(hostname)
//            .certificatePinner(certificatePinner)
            .build()
    }
    fun newBuildOkHttp(
        client: OkHttpClient,
        timeoutConnect: Long,
        timeoutReader: Long
    ): OkHttpClient {

        return client.newBuilder()
            .connectTimeout(timeoutConnect, TimeUnit.SECONDS)
           // .addInterceptor(SSLInterceptor())
           // .addInterceptor(BasicAuthInterceptor(SesCommonData.g().userName, SesCommonData.g().password))
            .readTimeout(timeoutReader, TimeUnit.SECONDS)
            .build()
    }


    fun createOkHttpCache(context: Context): Cache {
        return Cache(context.cacheDir, Long.MAX_VALUE)
    }

    fun createCertificatePinner(domain: String, cert1: String,cert2: String, cert3: String ): CertificatePinner {
        //LogSes.d(Tags.TAG, cert1 +"-"+ cert2 +"-"+ cert3)
        return CertificatePinner.Builder()
            .add(domain, "$cert1" )
            .add(domain, "$cert2" )
            .add(domain, "$cert3" )
            .build()
    }

    private fun getTrustValue(tmf2: TrustManagerFactory): X509TrustManager {
        return tmf2.trustManagers[0] as X509TrustManager
    }

    private fun getKeyStore(certValue: String): KeyStore? {
        try {
            val cf =
                CertificateFactory.getInstance("X.509")
            val caInput: InputStream = ByteArrayInputStream(
                Base64.decode(
                    certValue,
                    Base64.DEFAULT
                )
            )
            val ca: Certificate
            ca = try {
                cf.generateCertificate(caInput)
                //LogVnp.d("ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close()
            }
            val keyStoreType = KeyStore.getDefaultType()
            val keyStore = KeyStore.getInstance(keyStoreType)
            keyStore.load(null, null)
            keyStore.setCertificateEntry("ca", ca)
            return keyStore
        } catch (e: java.lang.Exception) {
        }
        return null
    }

    fun createGson(): Gson {
        return GsonBuilder().create()
    }

    fun createMediaType(): MediaType? {
        return MediaType.parse("application/octet-stream")
    }

    fun createService(client: OkHttpClient): ApiService {
        return BusMainRemote(client).getService()
    }


}