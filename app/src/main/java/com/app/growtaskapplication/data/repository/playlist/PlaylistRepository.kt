package com.app.growtaskapplication.data.repository.playlist

import com.app.growtaskapplication.data.api.ApiService
import com.app.growtaskapplication.data.model.Item
import com.app.growtaskapplication.data.model.album.SearchAlbumResponse
import com.app.growtaskapplication.data.model.playlist.PlaylistSearchResponse
import com.app.growtaskapplication.utills.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named

class PlaylistRepository @Inject constructor(@Named("commonService") val apiService: ApiService) {

    fun searchPlaylist(queryMap: HashMap<String, String>): Flow<Resource<PlaylistSearchResponse>> =
        flow {
            emit(Resource.loading())
            emit(Resource.success(apiService.searchPlaylist(queryMap)))
        }.catch {
            emit(Resource.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun getPlaylist(playListId:String): Flow<Resource<Item>> =
        flow {
            emit(Resource.loading())
            emit(Resource.success(apiService.getPlayList(playListId)))
        }.catch {
            emit(Resource.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)

}