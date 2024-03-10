package com.app.growtaskapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.growtaskapplication.data.model.album.SearchAlbumResponse
import com.app.growtaskapplication.data.model.artist.ArtistSearchResponse
import com.app.growtaskapplication.data.model.playlist.PlaylistSearchResponse
import com.app.growtaskapplication.data.model.tracks.TracksSearchResponse
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

    private val _albums = MutableStateFlow<Resource<SearchAlbumResponse>?>(null)
    val albums: StateFlow<Resource<SearchAlbumResponse>?> = _albums

    private val _artist = MutableStateFlow<Resource<ArtistSearchResponse>?>(null)
    val artist: StateFlow<Resource<ArtistSearchResponse>?> = _artist

    private val _playlist = MutableStateFlow<Resource<PlaylistSearchResponse>?>(null)
    val playlist: StateFlow<Resource<PlaylistSearchResponse>?> = _playlist

    private val _track = MutableStateFlow<Resource<TracksSearchResponse>?>(null)
    val track: StateFlow<Resource<TracksSearchResponse>?> = _track

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