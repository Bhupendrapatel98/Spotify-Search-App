package com.app.growtaskapplication.data.repository.artist

import com.app.growtaskapplication.data.api.ApiService
import com.app.growtaskapplication.data.model.Item
import com.app.growtaskapplication.data.model.artist.ArtistSearchResponse
import com.app.growtaskapplication.utills.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named

class ArtistRepository @Inject constructor(@Named("commonService") val apiService: ApiService) {

    fun searchArtist(queryMap: HashMap<String, String>): Flow<Resource<ArtistSearchResponse>> = flow {
        emit(Resource.loading())
        emit(Resource.success(apiService.searchArtist(queryMap)))
    }.catch {
        emit(Resource.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun getArtist(artistId:String): Flow<Resource<Item>> = flow {
        emit(Resource.loading())
        emit(Resource.success(apiService.getArtist(artistId)))
    }.catch {
        emit(Resource.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

}