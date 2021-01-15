package com.example.mybaseproject.networks

import android.text.TextUtils
import com.example.mybaseproject.networks.entities.ResponseEntity
import com.example.mybaseproject.utils.SesUIModel
import com.example.mybaseproject.utils.extensions.safeLog
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

//ErrorHandlingFactory
fun convertToBaseException(throwable: Throwable): BaseException =
    when (throwable) {
        is HttpException -> {
            val response = throwable.response()
            val mid:Int = getMid(response)
            var code = response?.headers()?.get("code")?:""

            if (response?.errorBody() == null) {
                BaseException.toHttpError(
                    response = response,
                    mid = mid,
                    code = code
                )
            }

            val serverErrorResponseBody = try {
                response?.errorBody()?.string() ?: ""
            } catch (e: Exception) {
                e.safeLog()
                ""
            }

            val serverErrorResponse =
                try {
                    SesUIModel.getInstance().provideGSon().fromJson(serverErrorResponseBody, ServerResponseEntity::class.java)
                } catch (e: Exception) {
                    e.safeLog()
                    ServerResponseEntity()
                }

            if (serverErrorResponse != null) {
                if(TextUtils.isEmpty(code))
                    code = serverErrorResponse.code?:"-1"
                serverErrorResponse.source = serverErrorResponseBody
                BaseException.toServerError(
                    serverErrorResponse = serverErrorResponse,
                    mid = mid,
                    code = code
                )
            } else {
                BaseException.toHttpError(
                    response = response,
                    mid = mid,
                    code = code
                )
            }
        }

        else -> BaseException.toUnexpectedError(throwable,0,null)
    }

fun getMid(response: Response<*>?): Int {
    try {
        return response?.headers()?.get("mid")!!.toInt()
    }catch (e:java.lang.Exception){
        e.safeLog()
    }
    return 0
}

class BaseException(
    val errorType: ErrorType,
    val serverErrorResponse: ServerResponseEntity? = null,
    val response: Response<*>? = null,
    val mid:Int = 0,
    val code:String?=null,
    cause: Throwable? = null
) : RuntimeException(cause?.message, cause) {

    override val message: String?
        get() = when (errorType) {
            ErrorType.HTTP -> response?.message()

            ErrorType.NETWORK -> cause?.message

            ErrorType.SERVER -> serverErrorResponse?.desc

            ErrorType.UNEXPECTED -> cause?.message
        }

    companion object {
        fun toHttpError(response: Response<*>?, mid:Int, code:String?) =
            BaseException(
                errorType = ErrorType.HTTP,
                response = response,
                mid = mid,
                code =  code
            )

        fun toNetworkError(cause: Throwable, mid:Int, code:String?) =
            BaseException(
                errorType = ErrorType.NETWORK,
                cause = cause,
                mid = mid,
                code =  code
            )

        fun toServerError(serverErrorResponse: ServerResponseEntity, mid:Int, code:String?) =
            BaseException(
                errorType = ErrorType.SERVER,
                serverErrorResponse = serverErrorResponse,
                mid = mid,
                code =  code
            )

        fun toUnexpectedError(cause: Throwable, mid:Int, code:String?) =
            BaseException(
                errorType = ErrorType.UNEXPECTED,
                cause = cause,
                mid = mid,
                code =  code
            )
    }
}

/**
 * Identifies the error type which triggered a [BaseException]
 */
enum class ErrorType {
    /**
     * An [IOException] occurred while communicating to the server.
     */
    NETWORK,

    /**
     * A non-2xx HTTP status code was received from the server.
     */
    HTTP,

    /**
     * A error server with code & message
     */
    SERVER,

    /**
     * An internal error occurred while attempting to execute a request. It is best practice to
     * re-throw this exception so your application crashes.
     */
    UNEXPECTED
}
class ServerResponseEntity : ResponseEntity<Any>()

