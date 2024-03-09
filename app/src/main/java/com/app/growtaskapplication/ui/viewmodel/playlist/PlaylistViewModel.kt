package com.app.growtaskapplication.ui.viewmodel.playlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.growtaskapplication.data.model.Item
import com.app.growtaskapplication.data.model.album.SearchAlbumResponse
import com.app.growtaskapplication.data.model.playlist.PlaylistSearchResponse
import com.app.growtaskapplication.data.repository.album.AlbumRepository
import com.app.growtaskapplication.data.repository.playlist.PlaylistRepository
import com.app.growtaskapplication.utills.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel
@Inject constructor(private val repository: PlaylistRepository) : ViewModel() {

    val searchFlowQuery = MutableStateFlow("")

    private val _playlist = MutableStateFlow<Resource<PlaylistSearchResponse>?>(null)
    val playlist: StateFlow<Resource<PlaylistSearchResponse>?> = _playlist

    private val _playlistDetail = MutableStateFlow<Resource<Item>?>(null)
    val playlistDetail: StateFlow<Resource<Item>?> = _playlistDetail

    fun searchPlaylist(queryMap: HashMap<String, String>) {
        viewModelScope.launch {
            repository.searchPlaylist(queryMap).collect() {
                _playlist.value = it
            }
        }
    }

    fun getPlaylist(playListId:String) {
        viewModelScope.launch {
            repository.getPlaylist(playListId).collect() {
                _playlistDetail.value = it
            }
        }
    }

}