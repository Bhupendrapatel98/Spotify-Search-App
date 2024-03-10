package com.app.growtaskapplication.data.interceptor

import com.app.growtaskapplication.utills.TokenManager
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
    private val tokenManager: TokenManager, ) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val token = tokenManager.getToken()
        return chain.proceed(newRequestWithAccessToken(token.toString(), originalRequest))
    }

    private fun newRequestWithAccessToken(accessToken: String, request: Request): Request =
        request.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

}
