package com.app.growtaskapplication.data.repository

import com.app.growtaskapplication.data.api.ApiService
import com.app.growtaskapplication.data.local.SearchDao
import com.app.growtaskapplication.data.local.SearchDatabase
import com.app.growtaskapplication.data.model.SearchResponse
import com.app.growtaskapplication.utills.Resource
import com.app.growtaskapplication.utills.UserType
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import javax.annotation.meta.When

@RunWith(JUnit4::class)
class SearchUserRepositoryImplTest {

    @Mock
    lateinit var apiService: ApiService

    @Mock
    lateinit var searchDatabase: SearchDatabase

    @Mock
    lateinit var searchResponse: SearchResponse

    lateinit var searchUserRepositoryImpl: SearchUserRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        searchUserRepositoryImpl = SearchUserRepositoryImpl(apiService, searchDatabase)
    }

    @Test
    fun `test searchAlbum success`() = runBlocking {

        //Given
        val queryMap = HashMap<String, String>()
        queryMap["query"] = "Hello"
        queryMap["type"] = UserType.ALBUM.type
        queryMap["locale"] = "en-US"
        queryMap["offset"] = "0"
        queryMap["limit"] = "20"

        val expectedResponse = Resource.success(searchResponse)

        // Stubbing API response
        `when`(apiService.searchAlbum(queryMap)).thenReturn(searchResponse)

        // Perform the operation
        val result = searchUserRepositoryImpl.searchAlbum(queryMap, UserType.ALBUM).toList()

        assertEquals(2, result.size)
        //assertEquals(Resource.loading<SearchResponse>(), result[0])
        //assertEquals(Resource.Success(searchResponse), result[1])
    }
}