package com.example.mybaseproject.ui.bases

import android.widget.PopupMenu
import androidx.annotation.CallSuper
import androidx.lifecycle.*
import com.example.mybaseproject.constants.Messages
import com.example.mybaseproject.networks.ServerResponseEntity
import com.example.mybaseproject.networks.convertToBaseException
import com.example.mybaseproject.utils.extensions.SingleLiveEvent
import com.example.mybaseproject.utils.extensions.safeLog
import kotlinx.coroutines.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLPeerUnverifiedException


abstract class SesBaseViewModel : ViewModel() {
    val isLoading = MutableLiveData<Boolean>().apply { value = false }

    val message = MutableLiveData<String>()
    val noInternetConnectionEvent = SingleLiveEvent<Unit>()
    val connectTimeoutEvent = SingleLiveEvent<Unit>()
    val invalidCertificateEvent = SingleLiveEvent<Unit>()
    val serverErrorEvent = SingleLiveEvent<Unit>()
    val successFinishMessage = MutableLiveData<String>()
    val expireSessionEvent = MutableLiveData<String>()
    val activeAnotherDeviceEvent = MutableLiveData<String>()

    val errorFinishMessage = MutableLiveData<String>()
    val errorToHomeMesssage = MutableLiveData<String>()
    val errorSoftOTPInvalidate = MutableLiveData<String>()


    @CallSuper
    override fun onCleared() {
        super.onCleared()
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            throwable.safeLog()
            withContext(Dispatchers.Main) {
                when (throwable) {
                    is UnknownHostException -> {
                        noInternetConnectionEvent.call()
                    }
                    is SocketTimeoutException -> {
                        connectTimeoutEvent.call()
                    }
                    is SSLPeerUnverifiedException -> {
                        invalidCertificateEvent.call()
                    }
                    else -> {
                        val baseException = convertToBaseException(throwable)

                        if (baseException.serverErrorResponse == null) {
                            serverErrorEvent.call()
                            return@withContext
                        }  else if (baseException.code == "DEVICE_ROOT-01") {
                            errorSoftOTPInvalidate.value = baseException.serverErrorResponse.desc
                            return@withContext
                        } else if (!baseException.message.isNullOrEmpty() && baseException.message.equals("TO")) {
                            connectTimeoutEvent.call()
                            if (baseException.mid == Messages.BANNER_OFFER_DESTIONATION){
                                onError(
                                        baseException.mid,
                                        baseException.code,
                                        baseException.serverErrorResponse
                                )
                            }
                            return@withContext
                        }
                        onError(
                                baseException.mid,
                                baseException.code,
                                baseException.serverErrorResponse
                        )
                    }
                }
                hideLoading()
            }
        }
    }


    open fun onError(mid: Int, code: String?, source: ServerResponseEntity?) {
        message.value = source?.desc
    }

    private fun hideLoading() {
        isLoading.value = false
    }

    private fun showLoading() {
        isLoading.value = true
    }

    val network = viewModelScope + exceptionHandler

    fun launch(isShow: Boolean = true, block: suspend CoroutineScope.() -> Unit): Job {
        showLoading()
        return network.launch {
            block.invoke(network)
            withContext(Dispatchers.Main) {
                hideLoading()
            }
            return@launch
        }
    }

    fun setForceShowIcon(popupMenu: PopupMenu) {
        try {
            val fields = popupMenu.javaClass.declaredFields
            for (field in fields) {
                if ("mPopup" == field.name) {
                    field.isAccessible = true
                    val menuPopupHelper = field.get(popupMenu)
                    val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                    val setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon", Boolean::class.javaPrimitiveType
                    )
                    setForceIcons.invoke(menuPopupHelper, true)
                    break
                }
            }
        } catch (e: Exception) {
        }
    }

}