package com.app.growtaskapplication.data.repository.tracks

import com.app.growtaskapplication.data.api.ApiService
import com.app.growtaskapplication.data.model.Item
import com.app.growtaskapplication.data.model.tracks.TracksSearchResponse
import com.app.growtaskapplication.utills.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named

class TracksRepository @Inject constructor(@Named("commonService") val apiService: ApiService) {

    fun searchTrack(queryMap: HashMap<String, String>): Flow<Resource<TracksSearchResponse>> =
        flow {
            emit(Resource.loading())
            emit(Resource.success(apiService.searchTracks(queryMap)))
        }.catch {
            emit(Resource.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun getTrack(trackId: String): Flow<Resource<Item>> =
        flow {
            emit(Resource.loading())
            emit(Resource.success(apiService.getTracks(trackId)))
        }.catch {
            emit(Resource.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)


}