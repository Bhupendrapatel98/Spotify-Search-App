package com.app.growtaskapplication.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.growtaskapplication.R
import com.app.growtaskapplication.data.model.Item
import com.app.growtaskapplication.data.model.SearchResponse
import com.app.growtaskapplication.ui.view.adapter.SearchItemAdapter
import com.app.growtaskapplication.databinding.FragmentAlbumBinding
import com.app.growtaskapplication.ui.viewmodel.SearchUserViewModel
import com.app.growtaskapplication.utills.Resource
import com.app.growtaskapplication.utills.NetworkUtils
import com.app.growtaskapplication.utills.UserType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlbumFragment() : Fragment() {
    private lateinit var fragmentAlbumBinding: FragmentAlbumBinding
    private lateinit var albumAdapter: SearchItemAdapter
    private var albumList: MutableList<Item> = mutableListOf()
    private val searchUserViewModel: SearchUserViewModel by activityViewModels()

    @Inject
    lateinit var networkUtils: NetworkUtils

    @OptIn(FlowPreview::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentAlbumBinding = FragmentAlbumBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            searchUserViewModel.searchFlowQuery.debounce(500)
                .distinctUntilChanged()
                .collect {
                    searchUserViewModel.search(UserType.ALBUM)
                }
        }

        setUpAdapter()
        attachObservers()

        return fragmentAlbumBinding.root
    }

    private fun setUpAdapter() {
        albumAdapter = SearchItemAdapter(albumList, UserType.ALBUM) { type, id ->
            val bundle = Bundle()
            bundle.putString("type", type.type)
            bundle.putString("id", id)
            Navigation.findNavController(fragmentAlbumBinding.root)
                .navigate(R.id.action_homeFragment_to_artistDetailFragment, bundle)
        }
        fragmentAlbumBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        fragmentAlbumBinding.recyclerView.adapter = albumAdapter
    }

    private fun attachObservers() {

        lifecycleScope.launch {
            searchUserViewModel.albums.collect {
                when (it) {
                    is Resource.Loading -> {
                        //show loader
                    }
                    is Resource.Failed -> {
                        //handle failure
                    }
                    is Resource.Success -> {
                        val dataFiltered = it.data.albums?.items?.filter { item ->
                            item.type == "album"
                        }
                        handleSuccess(dataFiltered)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun handleSuccess(data: List<Item>?) {
        albumList.clear()
        data?.let { albumList.addAll(it) }
        albumAdapter.notifyDataSetChanged()
    }

}