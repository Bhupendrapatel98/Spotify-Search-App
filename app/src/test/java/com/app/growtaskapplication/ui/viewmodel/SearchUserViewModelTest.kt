package com.app.growtaskapplication.ui.viewmodel

import com.app.growtaskapplication.data.model.*
import com.app.growtaskapplication.data.repository.SearchUserRepositoryImpl
import com.app.growtaskapplication.utills.Resource
import com.app.growtaskapplication.utills.UserType
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class SearchUserViewModelTest {

    @Mock
    lateinit var searchUserRepositoryImpl: SearchUserRepositoryImpl

    private lateinit var searchUserViewModel: SearchUserViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)

        searchUserViewModel = SearchUserViewModel(searchUserRepositoryImpl)
    }

    @Test
    fun testSearch() = runBlocking {

        val query = "hello"
        val type = UserType.ALBUM

        val expectedSearchResponse = mock(SearchResponse::class.java)
        val expectedResource = Resource.success(expectedSearchResponse)

        //Given
        val queryMap = HashMap<String, String>()
        queryMap["query"] = query
        queryMap["type"] = type.type
        queryMap["locale"] = "en-US"
        queryMap["offset"] = "0"
        queryMap["limit"] = "20"

        // Mocking searchAlbum method behavior
        `when`(searchUserRepositoryImpl.searchAlbum(queryMap, type)).thenReturn(flowOf(expectedResource))

        //When
        searchUserViewModel.searchFlowQuery.value = query
        searchUserViewModel.search(type)

        //Then
        val actualResource = searchUserViewModel.albums.first()
        assertEquals(expectedResource, actualResource)
    }
}