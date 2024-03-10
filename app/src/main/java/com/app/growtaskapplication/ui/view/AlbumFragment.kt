package com.app.growtaskapplication.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.growtaskapplication.R
import com.app.growtaskapplication.data.model.Item
import com.app.growtaskapplication.data.model.album.SearchAlbumResponse
import com.app.growtaskapplication.ui.view.adapter.SearchItemAdapter
import com.app.growtaskapplication.databinding.FragmentAlbumBinding
import com.app.growtaskapplication.ui.viewmodel.SearchUserViewModel
import com.app.growtaskapplication.utills.Resource
import com.app.growtaskapplication.utills.NetworkUtils
import com.app.growtaskapplication.utills.OnItemClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AlbumFragment : Fragment(), OnItemClick {
    private lateinit var fragmentAlbumBinding: FragmentAlbumBinding
    private lateinit var albumAdapter: SearchItemAdapter
    private var queryMap: HashMap<String, String> = hashMapOf()
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
                .filter { query ->
                    return@filter query.isNotEmpty()
                }.collect {
                    if (networkUtils.isInterNetAvailable()) {
                        searchAlbum(it)
                    }
                }
        }

        if (!networkUtils.isInterNetAvailable()) {
            searchAlbum("")
        }

        attachObservers()
        return fragmentAlbumBinding.root
    }

    private fun searchAlbum(str: String) {
        queryMap["query"] = str
        queryMap["type"] = "album"
        queryMap["locale"] = "en-US"
        queryMap["offset"] = "0"
        queryMap["limit"] = "20"
        searchUserViewModel.searchAlbum(queryMap)
    }


    private fun attachObservers() {
        lifecycleScope.launch {
            searchUserViewModel.albums.collect {
                when (it) {
                    is Resource.Loading -> {
                    }
                    is Resource.Failed -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success -> {
                        Log.d("Success", "attachObservers: " + it.data)
                        handleSuccess(it.data)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun handleSuccess(data: SearchAlbumResponse) {
        albumList = data.albums!!.items.toMutableList()
        albumAdapter = SearchItemAdapter(albumList, context, "album", this)
        fragmentAlbumBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        fragmentAlbumBinding.recyclerView.adapter = albumAdapter
    }

    override fun onClick(type: String, id: String) {
        val bundle = Bundle()
        bundle.putString("type", type)
        bundle.putString("id", id)
        Navigation.findNavController(fragmentAlbumBinding.root)
            .navigate(R.id.action_homeFragment_to_artistDetailFragment, bundle)
    }
}