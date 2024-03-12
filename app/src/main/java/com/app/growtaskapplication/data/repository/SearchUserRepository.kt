package com.app.growtaskapplication.data.repository

import com.app.growtaskapplication.data.api.ApiService
import com.app.growtaskapplication.data.local.SearchDatabase
import com.app.growtaskapplication.data.model.Data
import com.app.growtaskapplication.data.model.SearchResponse
import com.app.growtaskapplication.utills.NetworkUtils
import com.app.growtaskapplication.utills.Resource
import com.app.growtaskapplication.utills.UserType
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

    fun searchAlbum(
        queryMap: HashMap<String, String>,
        userType: UserType
    ): Flow<Resource<SearchResponse>> = flow {

        emit(Resource.loading())

        if (networkUtils.isInterNetAvailable() && queryMap["query"].toString().isNotEmpty()) {
            val apiResponse = apiService.searchAlbum(queryMap)
            val item = when (userType) {
                UserType.ALBUM -> apiResponse.albums!!.items
                UserType.ARTIST -> apiResponse.artists!!.items
                UserType.PLAYLIST -> apiResponse.playlists!!.items
                UserType.TRACKS -> apiResponse.tracks!!.items
            }

            if (item != null) {
                searchDatabase.searchDao().apply {
                    deleteByType(userType.type)
                    insertSearch(item)
                }
            }
            emit(Resource.success(apiResponse))
        } else {
            val item = searchDatabase.searchDao().getAllSearch()
            val albums = Data("", item, 1, "", 1, "", 1)
            val searchAlbumResponse = SearchResponse(albums, albums, albums, albums)
            emit(Resource.success(searchAlbumResponse))
        }
    }.catch {
        emit(Resource.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

}