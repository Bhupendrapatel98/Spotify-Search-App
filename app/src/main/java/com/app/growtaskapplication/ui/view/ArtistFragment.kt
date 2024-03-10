package com.app.growtaskapplication.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArtistFragment : Fragment() {

    lateinit var fragmentArtistBinding: FragmentArtistBinding
    private var queryMap: HashMap<String, String> = hashMapOf()
    private var artistList: MutableList<Item> = mutableListOf()
    private lateinit var artistAdapter: SearchItemAdapter
    private val searchUserViewModel: SearchUserViewModel by activityViewModels()

    @OptIn(FlowPreview::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        fragmentArtistBinding = FragmentArtistBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            searchUserViewModel.searchFlowQuery.debounce(500)
                .distinctUntilChanged()
                .filter {query->
                    return@filter query.isNotEmpty()
                }.collect {
                searchAlbum(it)
            }
        }

        attachObservers()

        artistAdapter = SearchItemAdapter(artistList,"artist"){ type, id->
            val bundle = Bundle()
            bundle.putString("type", type)
            bundle.putString("id", id)
            Navigation.findNavController(fragmentArtistBinding.root).navigate(R.id.action_homeFragment_to_artistDetailFragment,bundle)
        }
        fragmentArtistBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        fragmentArtistBinding.recyclerView.adapter = artistAdapter

        return fragmentArtistBinding.root
    }

    private fun searchAlbum(str: String) {
        queryMap["query"] = str
        queryMap["type"] = "artist"
        queryMap["locale"] = "en-US"
        queryMap["offset"] = "0"
        queryMap["limit"] = "20"
        searchUserViewModel.searchArtist(queryMap)
       // searchUserViewModel.searchAlbum(queryMap,"artist")
    }

    private fun attachObservers() {
        lifecycleScope.launch {
            searchUserViewModel.artist.collect {
                when (it) {
                    is Resource.Loading -> {
                      //  ProgressDialog.show(context!!)
                    }
                    is Resource.Failed -> {
                       // ProgressDialog.dismiss()
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success -> {
                      //  ProgressDialog.dismiss()
                        handleSuccess(it.data)
                    }
                    else -> {
                      //  ProgressDialog.dismiss()
                    }
                }
            }
        }
    }

    private fun handleSuccess(data: SearchResponse) {
        artistList.clear()
        data.artists?.let { artistList.addAll(it.items) }
        artistAdapter.notifyDataSetChanged()
    }
}