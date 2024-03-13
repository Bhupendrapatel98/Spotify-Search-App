package com.app.growtaskapplication.data.repository

import android.util.Log
import com.app.growtaskapplication.data.api.ApiService
import com.app.growtaskapplication.data.interceptor.HeaderInterceptor
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
    @Named("commonService") val apiService: ApiService, private val searchDatabase: SearchDatabase
) {

    fun searchAlbum(
        queryMap: HashMap<String, String>, userType: UserType
    ): Flow<Resource<SearchResponse>> = flow {

        emit(Resource.loading())

        //getting Data from Remote Source
        val apiResponse = apiService.searchAlbum(queryMap)
        val item = when (userType) {
            UserType.ALBUM -> apiResponse.albums!!.items
            UserType.ARTIST -> apiResponse.artists!!.items
            UserType.PLAYLIST -> apiResponse.playlists!!.items
            UserType.TRACKS -> apiResponse.tracks!!.items
        }

        //removing data form local and saving again when get new data
        if (item != null) {
            searchDatabase.searchDao().apply {
                deleteByType(userType.type)
                insertSearch(item)
            }
        }
        emit(Resource.success(apiResponse))

    }.catch {
        when (it) {
            is HeaderInterceptor.NoConnectivityException -> {
                //when get no-internet exception then showing local data
                emit(Resource.success(showCacheData()))
            }
            else -> {
                //when getting other exceptions then showing message to user or we can show local data
                emit(Resource.success(showCacheData()))
                //emit(Resource.failed(it.message.toString()))
            }
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun showCacheData(): SearchResponse {
        val item = searchDatabase.searchDao().getAllSearch()
        val albums = Data("", item, 1, "", 1, "", 1)
        return SearchResponse(albums, albums, albums, albums)
    }

}