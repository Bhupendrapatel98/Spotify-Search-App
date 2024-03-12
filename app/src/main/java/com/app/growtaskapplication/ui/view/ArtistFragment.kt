package com.app.growtaskapplication.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.growtaskapplication.R
import com.app.growtaskapplication.data.model.Item
import com.app.growtaskapplication.data.model.SearchResponse
import com.app.growtaskapplication.databinding.FragmentArtistBinding
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
class ArtistFragment : Fragment() {

    lateinit var fragmentArtistBinding: FragmentArtistBinding
    private var artistList: MutableList<Item> = mutableListOf()
    private lateinit var artistAdapter: SearchItemAdapter
    private val searchUserViewModel: SearchUserViewModel by activityViewModels()

    @OptIn(FlowPreview::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentArtistBinding = FragmentArtistBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            searchUserViewModel.searchFlowQuery.debounce(500)
                .distinctUntilChanged()
                .collect {
                    searchUserViewModel.search(UserType.ARTIST)
                }
        }

        setUpAdapter()
        attachObservers()

        return fragmentArtistBinding.root
    }

    private fun setUpAdapter() {
        artistAdapter = SearchItemAdapter(artistList, UserType.ARTIST) { type, id ->
            val bundle = Bundle()
            bundle.putString("type", type.type)
            bundle.putString("id", id)
            Navigation.findNavController(fragmentArtistBinding.root)
                .navigate(R.id.action_homeFragment_to_artistDetailFragment, bundle)
        }
        fragmentArtistBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        fragmentArtistBinding.recyclerView.adapter = artistAdapter
    }

    private fun attachObservers() {
        lifecycleScope.launch {
            searchUserViewModel.artist.collect {
                when (it) {
                    is Resource.Loading -> {
                        // show Loader
                    }
                    is Resource.Failed -> {
                        //handle failure
                    }
                    is Resource.Success -> {
                        val dataFiltered = it.data.artists?.items?.filter { item -> item.type == "artist" }
                        handleSuccess(dataFiltered)
                    }
                    else -> {
                        // handle else
                    }
                }
            }
        }
    }

    private fun handleSuccess(item: List<Item>?) {
        artistList.clear()
        item?.let { artistList.addAll(it) }
        artistAdapter.notifyDataSetChanged()
    }
}