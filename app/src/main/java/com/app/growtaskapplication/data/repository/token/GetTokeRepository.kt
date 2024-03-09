package com.app.growtaskapplication.data.repository.token

import com.app.growtaskapplication.data.api.ApiService
import com.app.growtaskapplication.data.model.token.GetTokenResponse
import com.app.growtaskapplication.utills.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Named

class GetTokeRepository @Inject constructor(
    @Named("tokenService") private val apiService: ApiService
) {
    fun getToken(
        grantType: String,
        clientId: String,
        clientSecret: String
    ): Flow<Resource<GetTokenResponse>> = flow {
        emit(Resource.loading())
        emit(Resource.success(apiService.getToken(grantType, clientId, clientSecret)))
    }.catch {
        emit(Resource.failed(it.message.toString()))
    }
}