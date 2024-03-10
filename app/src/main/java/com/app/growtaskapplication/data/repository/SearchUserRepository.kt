package com.app.growtaskapplication.data.repository

import com.app.growtaskapplication.data.api.ApiService
import com.app.growtaskapplication.data.local.SearchDatabase
import com.app.growtaskapplication.data.model.album.Albums
import com.app.growtaskapplication.data.model.album.SearchAlbumResponse
import com.app.growtaskapplication.data.model.artist.ArtistSearchResponse
import com.app.growtaskapplication.data.model.playlist.PlaylistSearchResponse
import com.app.growtaskapplication.data.model.tracks.TracksSearchResponse
import com.app.growtaskapplication.utills.NetworkUtils
import com.app.growtaskapplication.utills.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named

class SearchUserRepository @Inject constructor(
    @Named("commonService") val apiService: ApiService,
    private val searchDatabase: SearchDatabase,
    private val networkUtils: NetworkUtils
) {

    fun searchAlbum(queryMap: HashMap<String, String>): Flow<Resource<SearchAlbumResponse>> = flow {
        emit(Resource.loading())

        if (networkUtils.isInterNetAvailable()) {
            searchDatabase.searchDao().clearData()
            val response = apiService.searchAlbum(queryMap).albums.items
            searchDatabase.searchDao().insertSearch(response)
            emit(Resource.success(apiService.searchAlbum(queryMap)))
        } else {
            val item = searchDatabase.searchDao().getAllSearch()
            val albums = Albums("", item, 1, "", 1, "", 1)
            val searchAlbumResponse = SearchAlbumResponse(albums)
            emit(Resource.success(searchAlbumResponse))
        }
    }.catch {
        emit(Resource.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun searchArtist(queryMap: HashMap<String, String>): Flow<Resource<ArtistSearchResponse>> =
        flow {
            emit(Resource.loading())
            emit(Resource.success(apiService.searchArtist(queryMap)))
        }.catch {
            emit(Resource.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun searchPlaylist(queryMap: HashMap<String, String>): Flow<Resource<PlaylistSearchResponse>> =
        flow {
            emit(Resource.loading())
            emit(Resource.success(apiService.searchPlaylist(queryMap)))
        }.catch {
            emit(Resource.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)

    fun searchTrack(queryMap: HashMap<String, String>): Flow<Resource<TracksSearchResponse>> =
        flow {
            emit(Resource.loading())
            emit(Resource.success(apiService.searchTracks(queryMap)))
        }.catch {
            emit(Resource.failed(it.message.toString()))
        }.flowOn(Dispatchers.IO)
}