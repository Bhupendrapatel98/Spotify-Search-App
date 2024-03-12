package com.app.growtaskapplication.ui.view

import android.os.Bundle
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
import com.app.growtaskapplication.databinding.FragmentPlaylistBinding
import com.app.growtaskapplication.ui.view.adapter.SearchItemAdapter
import com.app.growtaskapplication.ui.viewmodel.SearchUserViewModel
import com.app.growtaskapplication.utills.Resource
import com.app.growtaskapplication.utills.UserType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlaylistFragment : Fragment() {

    private lateinit var fragmentPlaylistBinding: FragmentPlaylistBinding
    private var playlistList: MutableList<Item> = mutableListOf()
    private lateinit var playlistAdapter: SearchItemAdapter
    private val searchUserViewModel: SearchUserViewModel by activityViewModels()

    @OptIn(FlowPreview::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentPlaylistBinding = FragmentPlaylistBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            searchUserViewModel.searchFlowQuery.debounce(500)
                .distinctUntilChanged()
                .collect {
                    searchUserViewModel.search(UserType.PLAYLIST)
                }
        }

        setUpAdapter()
        attachObservers()

        return fragmentPlaylistBinding.root
    }

    private fun setUpAdapter() {
        playlistAdapter = SearchItemAdapter(playlistList,UserType.PLAYLIST){ type, id->
            val bundle = Bundle()
            bundle.putString("type", type.type)
            bundle.putString("id", id)
            Navigation.findNavController(fragmentPlaylistBinding.root).navigate(R.id.action_homeFragment_to_artistDetailFragment,bundle)
        }
        fragmentPlaylistBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        fragmentPlaylistBinding.recyclerView.adapter = playlistAdapter
    }

    private fun attachObservers() {
        lifecycleScope.launch {
            searchUserViewModel.playlist.collect {
                when (it) {
                    is Resource.Loading -> {
                        //show Loader
                    }
                    is Resource.Failed -> {
                       //handle failure
                    }
                    is Resource.Success -> {
                        val dataFiltered = it.data.playlists?.items?.filter { item ->
                            item.type == "playlist"
                        }
                        handleSuccess(dataFiltered)
                    }
                    else -> {
                        //handle else
                    }
                }
            }
        }
    }

    private fun handleSuccess(item: List<Item>?) {
        playlistList.clear()
        item?.let { playlistList.addAll(it) }
        playlistAdapter.notifyDataSetChanged()
    }

}