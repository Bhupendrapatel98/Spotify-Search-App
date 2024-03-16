package com.app.growtaskapplication.data.interceptor

import com.app.growtaskapplication.utills.NetworkUtils
import com.app.growtaskapplication.utills.TokenManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
    private val tokenManager: TokenManager, private val networkUtils: NetworkUtils
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val token = tokenManager.getToken()

        //when no internet show custom exception
        if (!networkUtils.isInterNetAvailable()) {
            throw NoConnectivityException()
        }

        //need to handle token expire

        return chain.proceed(newRequestWithAccessToken(token.toString(), originalRequest))
    }

    private fun newRequestWithAccessToken(accessToken: String, request: Request): Request =
        request.newBuilder().header("Authorization", "Bearer $accessToken").build()

    class NoConnectivityException(message: String="No internet connection") : IOException(message)

}
