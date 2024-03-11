package com.app.growtaskapplication.data.repository

import android.util.Log
import com.app.growtaskapplication.data.api.ApiService
import com.app.growtaskapplication.data.local.SearchDatabase
import com.app.growtaskapplication.data.model.Data
import com.app.growtaskapplication.data.model.Item
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

            emit(Resource.success(apiService.searchAlbum(queryMap)))

            if (userType == UserType.ALBUM) {
                searchDatabase.searchDao().clearData()
                val response = apiService.searchAlbum(queryMap).albums!!.items
                searchDatabase.searchDao().insertSearch(response)
            }

        } else {
            val item = searchDatabase.searchDao().getAllSearch()
            val albums = Data("", item, 1, "", 1, "", 1)
            val searchAlbumResponse = SearchResponse(null, albums, null, null)
            emit(Resource.success(searchAlbumResponse))
        }
    }.catch {
        emit(Resource.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

}