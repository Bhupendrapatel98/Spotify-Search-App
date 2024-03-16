package com.app.growtaskapplication.utills

import android.util.Log
import com.app.growtaskapplication.data.interceptor.HeaderInterceptor
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.HttpException

fun handleNetworkException(e: Throwable): String {
    val errorMessage = when (e) {
        is HeaderInterceptor.NoConnectivityException -> {
            e.message
        }
        is HttpException -> {
            getServerError(e.code(), e.response()?.errorBody())
        }
        else -> "Failed: ${e.message}"
    }
    return errorMessage.toString()
}

fun getServerError(responseCode: Int, responseBody: ResponseBody?): String? {
    var serverHandling = "Something went wrong"
    try {
       responseBody?.let {
           val responseBodyString = it.string()
            val gson = GsonBuilder().create()
            val errorResponse: ErrorResponse? = gson.fromJson(responseBodyString, ErrorResponse::class.java)

           errorResponse?.error?.message?.let { message ->
                serverHandling = message
            }
            return serverHandling
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return serverHandling
}
