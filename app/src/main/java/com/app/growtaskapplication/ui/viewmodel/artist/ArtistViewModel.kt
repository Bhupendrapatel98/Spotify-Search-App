package com.app.growtaskapplication.ui.viewmodel.artist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.growtaskapplication.data.model.Item
import com.app.growtaskapplication.data.model.artist.ArtistSearchResponse
import com.app.growtaskapplication.data.repository.artist.ArtistRepository
import com.app.growtaskapplication.utills.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtistViewModel @Inject constructor(private val repository: ArtistRepository) : ViewModel() {

    val searchFlowQuery = MutableStateFlow("")

    private val _artist = MutableStateFlow<Resource<ArtistSearchResponse>?>(null)
    val artist: StateFlow<Resource<ArtistSearchResponse>?> = _artist

    private val _artistDetail = MutableStateFlow<Resource<Item>?>(null)
    val artistDetail: StateFlow<Resource<Item>?> = _artistDetail

    fun searchArtist(queryMap: HashMap<String, String>) {
        viewModelScope.launch {
            repository.searchArtist(queryMap).collect() {
                _artist.value = it
            }
        }
    }

    fun getArtist(artistId:String) {
        viewModelScope.launch {
            repository.getArtist(artistId).collect() {
                _artistDetail.value = it
            }
        }
    }

}