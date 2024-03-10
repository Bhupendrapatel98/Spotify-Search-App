package com.app.growtaskapplication.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.growtaskapplication.data.model.Data
import com.app.growtaskapplication.data.model.SearchResponse
import com.app.growtaskapplication.data.repository.SearchUserRepository
import com.app.growtaskapplication.utills.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchUserViewModel @Inject constructor(private val repository: SearchUserRepository) : ViewModel() {

    val searchFlowQuery = MutableStateFlow("")

    private val _albums = MutableStateFlow<Resource<SearchResponse>?>(null)
    val albums: StateFlow<Resource<SearchResponse>?> = _albums

    private val _artist = MutableStateFlow<Resource<SearchResponse>?>(null)
    val artist: StateFlow<Resource<SearchResponse>?> = _artist

    private val _playlist = MutableStateFlow<Resource<SearchResponse>?>(null)
    val playlist: StateFlow<Resource<SearchResponse>?> = _playlist

    private val _track = MutableStateFlow<Resource<SearchResponse>?>(null)
    val track: StateFlow<Resource<SearchResponse>?> = _track

    fun searchAlbum(queryMap: HashMap<String, String>) {
        viewModelScope.launch {
            repository.searchAlbum(queryMap).collect{
                _albums.value = it
            }
        }
    }

    fun searchArtist(queryMap: HashMap<String, String>) {
        viewModelScope.launch {
            repository.searchArtist(queryMap).collect() {
                _artist.value = it
            }
        }
    }

    fun searchPlaylist(queryMap: HashMap<String, String>) {
        viewModelScope.launch {
            repository.searchPlaylist(queryMap).collect() {
                _playlist.value = it
            }
        }
    }

    fun searchTrack(queryMap: HashMap<String, String>) {
        viewModelScope.launch {
            repository.searchTrack(queryMap).collect() {
                _track.value = it
            }
        }
    }

}