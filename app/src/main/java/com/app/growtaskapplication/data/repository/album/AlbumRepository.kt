package com.app.growtaskapplication.data.repository.album

import com.app.growtaskapplication.data.api.ApiService
import com.app.growtaskapplication.data.local.SearchDatabase
import com.app.growtaskapplication.data.model.Item
import com.app.growtaskapplication.data.model.album.Albums
import com.app.growtaskapplication.data.model.album.SearchAlbumResponse
import com.app.growtaskapplication.utills.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named

class AlbumRepository @Inject constructor(@Named("commonService") val apiService: ApiService,
    private val searchDatabase: SearchDatabase) {

    fun searchAlbum(queryMap: HashMap<String, String>): Flow<Resource<SearchAlbumResponse>> = flow {
        emit(Resource.loading())
        emit(Resource.success(apiService.searchAlbum(queryMap)))

        //saving in Room Db
        val response = apiService.searchAlbum(queryMap).albums.items
        searchDatabase.searchDao().insertSearch(response)
        //val albums = Albums("",response,1,"",1,"",1)
        //val searchAlbumResponse = SearchAlbumResponse(albums)
        //emit(Resource.success(searchAlbumResponse))
    }.catch {
        emit(Resource.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

    fun getAlbum(albumId: String): Flow<Resource<Item>> = flow {
        emit(Resource.loading())
        emit(Resource.success(apiService.getAlbum(albumId)))
    }.catch {
        emit(Resource.failed(it.message.toString()))
    }.flowOn(Dispatchers.IO)

}