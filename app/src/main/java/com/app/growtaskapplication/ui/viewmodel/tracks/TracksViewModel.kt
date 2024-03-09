package com.app.growtaskapplication.ui.viewmodel.tracks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.growtaskapplication.data.model.Item
import com.app.growtaskapplication.data.model.playlist.PlaylistSearchResponse
import com.app.growtaskapplication.data.model.tracks.TracksSearchResponse
import com.app.growtaskapplication.data.repository.playlist.PlaylistRepository
import com.app.growtaskapplication.data.repository.tracks.TracksRepository
import com.app.growtaskapplication.utills.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TracksViewModel
@Inject constructor(private val repository: TracksRepository) : ViewModel() {

    val searchFlowQuery = MutableStateFlow("")

    private val _track = MutableStateFlow<Resource<TracksSearchResponse>?>(null)
    val track: StateFlow<Resource<TracksSearchResponse>?> = _track

    private val _trackDetail = MutableStateFlow<Resource<Item>?>(null)
    val trackDetail: StateFlow<Resource<Item>?> = _trackDetail

    fun searchTrack(queryMap: HashMap<String, String>) {
        viewModelScope.launch {
            repository.searchTrack(queryMap).collect() {
                _track.value = it
            }
        }
    }

    fun getTrack(trackId:String) {
        viewModelScope.launch {
            repository.getTrack(trackId).collect() {
                _trackDetail.value = it
            }
        }
    }

}