package com.app.growtaskapplication.data.repository

import com.app.growtaskapplication.data.model.SearchResponse
import com.app.growtaskapplication.utills.Resource
import com.app.growtaskapplication.utills.UserType
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun searchAlbum(queryMap: HashMap<String, String>, userType: UserType) : Flow<Resource<SearchResponse>>
}