package com.app.growtaskapplication.ui.viewmodel.album

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.growtaskapplication.data.model.Item
import com.app.growtaskapplication.data.model.album.SearchAlbumResponse
import com.app.growtaskapplication.data.repository.album.AlbumRepository
import com.app.growtaskapplication.utills.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(private val repository: AlbumRepository) : ViewModel() {

    val searchFlowQuery = MutableStateFlow("")

    private val _albums = MutableStateFlow<Resource<SearchAlbumResponse>?>(null)
    val albums: StateFlow<Resource<SearchAlbumResponse>?> = _albums

    private val _albumsDetail = MutableStateFlow<Resource<Item>?>(null)
    val albumsDetail: StateFlow<Resource<Item>?> = _albumsDetail

    fun searchAlbum(queryMap: HashMap<String, String>) {
        viewModelScope.launch {
            repository.searchAlbum(queryMap).collect{
                _albums.value = it
            }
        }
    }

    fun getAlbum(albumId:String) {
        viewModelScope.launch {
            repository.getAlbum(albumId).collect{
                _albumsDetail.value = it
            }
        }
    }

}