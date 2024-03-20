package com.app.growtaskapplication.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import com.app.growtaskapplication.data.model.*
import com.app.growtaskapplication.data.repository.SearchUserRepositoryImpl
import com.app.growtaskapplication.utills.Resource
import com.app.growtaskapplication.utills.UserType
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import javax.inject.Inject

@RunWith(JUnit4::class)
class SearchUserViewModelTest {

    @Mock
    lateinit var searchUserRepositoryImpl: SearchUserRepositoryImpl

    lateinit var searchUserViewModel: SearchUserViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(Dispatchers.Unconfined)
        searchUserViewModel = SearchUserViewModel(searchUserRepositoryImpl)
    }

    @Test
    fun testViewModel() = runBlocking {

        val spyResult = spy(searchUserViewModel)

        val query = "hello"
        val type = UserType.ALBUM

        val item = Item(
            "1", "album", null, null, "", "", "", 5, "", null, 5, "", true, "", null, null, null,
            100, explicit = true, is_local = true, preview_url = "", track_number = null)

        val listItem: List<Item> = listOf(item)
        val albums = Data("", listItem, 1, "", 1, "", 1)
        val expectedSearchResponse = SearchResponse(albums, null, null, null)
        val expectedResource = Resource.Success(expectedSearchResponse)

        val queryMap = HashMap<String, String>()
        queryMap["query"] = query
        queryMap["type"] = type.type
        queryMap["locale"] = "en-US"
        queryMap["offset"] = "0"
        queryMap["limit"] = "20"


        `when`(searchUserRepositoryImpl.searchAlbum(queryMap, type)).thenReturn(flowOf(expectedResource))
        searchUserViewModel.searchFlowQuery.value = query
        spyResult.search(type)
        assertEquals(expectedResource, searchUserViewModel.albums.value)
    }
}