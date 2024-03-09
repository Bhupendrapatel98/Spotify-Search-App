package com.app.growtaskapplication.ui.viewmodel.token

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.growtaskapplication.data.model.token.GetTokenResponse
import com.app.growtaskapplication.data.repository.token.GetTokeRepository
import com.app.growtaskapplication.utills.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetTokenViewModel @Inject constructor(private var tokenRepository: GetTokeRepository) :
    ViewModel() {

    private var _token = MutableStateFlow<Resource<GetTokenResponse>>(Resource.loading())
    val token: StateFlow<Resource<GetTokenResponse>> = _token

    fun getToken(grant_type: String, client_id: String, client_secret: String) {
        viewModelScope.launch {
            tokenRepository.getToken(grant_type, client_id, client_secret).collect {
                _token.value =it
            }
        }
    }
}