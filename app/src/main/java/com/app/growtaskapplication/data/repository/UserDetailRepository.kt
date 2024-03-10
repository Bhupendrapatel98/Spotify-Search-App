package com.app.growtaskapplication.data.repository

import com.app.growtaskapplication.data.api.ApiService
import com.app.growtaskapplication.data.model.Item
import com.app.growtaskapplication.utills.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named

class UserDetailRepository @Inject constructor(@Named("commonService") val apiService: ApiService) {
    fun getUserDetail(userId: String,type:String): Flow<Resource<Item>> = flow {
        emit(Resource.loading())
        when (type) {
            "artist" -> {
                emit(Resource.success(apiService.getArtist(userId)))
            }
            "album" -> {
                emit(Resource.success(apiService.getAlbum(userId)))
            }
            "playlist" -> {
                emit(Resource.success(apiService.getPlayList(userId)))
            }
            "tracks" -> {
                emit(Resource.success(apiService.getTracks(userId)))
            }
        }
    }.catch {
        emit(Resource.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)
}