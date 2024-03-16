package com.app.growtaskapplication.data.repository

import android.util.Log
import com.app.growtaskapplication.data.api.ApiService
import com.app.growtaskapplication.data.interceptor.HeaderInterceptor
import com.app.growtaskapplication.data.local.SearchDatabase
import com.app.growtaskapplication.data.model.Data
import com.app.growtaskapplication.data.model.SearchResponse
import com.app.growtaskapplication.utills.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Named

class SearchUserRepository @Inject constructor(
    @Named("commonService") val apiService: ApiService, private val searchDatabase: SearchDatabase) {

    fun searchAlbum(queryMap: HashMap<String, String>, userType: UserType): Flow<Resource<SearchResponse>> = flow {
        emit(Resource.loading())
        //getting Data from Remote Source
        val apiResponse = apiService.searchAlbum(queryMap)
        getDiffTypeResponse(userType, apiResponse)
        emit(Resource.success(apiResponse))
    }.catch {
        //getting data from Local Source in all error case
        emit(Resource.success(showCacheData()))

        //handle errors
        emit(Resource.failed(handleNetworkException(it)))
    }.flowOn(Dispatchers.IO)

    private suspend fun getDiffTypeResponse(userType: UserType, apiResponse: SearchResponse) {
        val item = when (userType) {
            UserType.ALBUM -> apiResponse.albums!!.items
            UserType.ARTIST -> apiResponse.artists!!.items
            UserType.PLAYLIST -> apiResponse.playlists!!.items
            UserType.TRACKS -> apiResponse.tracks!!.items
        }

        //removing data form local and saving again when get new data
        searchDatabase.searchDao().apply {
            deleteByType(userType.type)
            insertSearch(item)
        }
    }

    private suspend fun showCacheData(): SearchResponse {
        val item = searchDatabase.searchDao().getAllSearch()
        val albums = Data("", item, 1, "", 1, "", 1)
        return SearchResponse(albums, albums, albums, albums)
    }

}