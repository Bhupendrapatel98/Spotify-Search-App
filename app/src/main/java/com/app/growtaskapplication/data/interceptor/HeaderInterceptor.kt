package com.app.growtaskapplication.data.interceptor

import com.app.growtaskapplication.utills.TokenManager
import com.app.growtaskapplication.utills.TokenRefreshService
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.HttpURLConnection
import javax.inject.Inject

class HeaderInterceptor @Inject constructor(
    private val tokenManager: TokenManager,
    private val tokenRefreshService: TokenRefreshService
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val token = tokenManager.getToken()

        val response = chain.proceed(newRequestWithAccessToken(token.toString(), originalRequest))

        if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
            val newAccessToken = tokenManager.getToken()
            return if (newAccessToken != token) {
                chain.proceed(newRequestWithAccessToken(token.toString(), originalRequest))
            } else {
                tokenManager.clearData()
                tokenRefreshService.refreshToken()
                chain.proceed(newRequestWithAccessToken(token.toString(), originalRequest))
            }
        }
        return response
    }

    private fun newRequestWithAccessToken(accessToken: String, request: Request): Request =
        request.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

}
