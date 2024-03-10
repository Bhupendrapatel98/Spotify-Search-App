package com.app.growtaskapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.growtaskapplication.data.model.Item
import com.app.growtaskapplication.data.repository.UserDetailRepository
import com.app.growtaskapplication.utills.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(private val userDetailRepository: UserDetailRepository) : ViewModel(){

    private val _userDetail = MutableStateFlow<Resource<Item>?>(null)
    val userDetail: StateFlow<Resource<Item>?> = _userDetail

    fun getUserDetail(albumId:String,type:String) {
        viewModelScope.launch {
            userDetailRepository.getUserDetail(albumId,type).collect{
                _userDetail.value = it
            }
        }
    }
}