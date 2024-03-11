package com.app.growtaskapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.growtaskapplication.data.model.SearchResponse
import com.app.growtaskapplication.data.repository.SearchUserRepository
import com.app.growtaskapplication.utills.Resource
import com.app.growtaskapplication.utills.UserType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.collections.HashMap
import kotlin.math.log

@HiltViewModel
class SearchUserViewModel @Inject constructor(private val repository: SearchUserRepository) :
    ViewModel() {

    val searchFlowQuery = MutableStateFlow("")

    private val _albums = MutableStateFlow<Resource<SearchResponse>?>(null)
    val albums: StateFlow<Resource<SearchResponse>?> = _albums

    private val _artist = MutableStateFlow<Resource<SearchResponse>?>(null)
    val artist: StateFlow<Resource<SearchResponse>?> = _artist

    private val _playlist = MutableStateFlow<Resource<SearchResponse>?>(null)
    val playlist: StateFlow<Resource<SearchResponse>?> = _playlist

    private val _track = MutableStateFlow<Resource<SearchResponse>?>(null)
    val track: StateFlow<Resource<SearchResponse>?> = _track

    fun search(type: UserType) {
        viewModelScope.launch {
            val queryMap = HashMap<String, String>()
            queryMap["query"] = searchFlowQuery.value
            queryMap["type"] = type.type
            queryMap["locale"] = "en-US"
            queryMap["offset"] = "0"
            queryMap["limit"] = "20"

            repository.searchAlbum(queryMap,type).collect {
                when (type) {
                    UserType.ALBUM -> _albums.value = it
                    UserType.ARTIST -> _artist.value = it
                    UserType.PLAYLIST -> _playlist.value = it
                    UserType.TRACKS -> _track.value = it
                }
            }

        }
    }
}