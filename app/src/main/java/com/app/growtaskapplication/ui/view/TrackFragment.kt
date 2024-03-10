package com.app.growtaskapplication.ui.view

import android.os.Bundle
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
import com.app.growtaskapplication.data.model.SearchResponse
import com.app.growtaskapplication.databinding.FragmentTrackBinding
import com.app.growtaskapplication.ui.view.adapter.SearchItemAdapter
import com.app.growtaskapplication.ui.viewmodel.SearchUserViewModel
import com.app.growtaskapplication.utills.Resource
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class TrackFragment : Fragment() {

    private lateinit var fragmentTrackBinding: FragmentTrackBinding
    private var queryMap: HashMap<String, String> = hashMapOf()
    private var tracksList: MutableList<Item> = mutableListOf()
    private lateinit var trackAdapter: SearchItemAdapter
    private val searchUserViewModel: SearchUserViewModel by activityViewModels()

    @OptIn(FlowPreview::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentTrackBinding = FragmentTrackBinding.inflate(inflater, container, false)

        lifecycleScope.launch {
            searchUserViewModel.searchFlowQuery.debounce(500)
                .distinctUntilChanged()
                .filter {query->
                    return@filter query.isNotEmpty()
                }
                .collect {
                searchTrack(it)
            }
        }

        attachObservers()

        return fragmentTrackBinding.root
    }

    private fun searchTrack(str: String) {
        queryMap["query"] = str
        queryMap["type"] = "track"
        queryMap["locale"] = "en-US"
        queryMap["offset"] = "0"
        queryMap["limit"] = "20"
        searchUserViewModel.searchTrack(queryMap)
    }

    private fun attachObservers() {
        lifecycleScope.launch {
            searchUserViewModel.track.collect {
                when (it) {
                    is Resource.Loading -> {
                    }
                    is Resource.Failed -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success -> {
                        handleSuccess(it.data)
                    }
                    else -> {}
                }
            }
        }
    }

    private fun handleSuccess(data: SearchResponse) {
        tracksList = data.tracks!!.items.toMutableList()
        trackAdapter = SearchItemAdapter(tracksList,"tracks",){type,id->
            val bundle = Bundle()
            bundle.putString("type", type)
            bundle.putString("id", id)
            Navigation.findNavController(fragmentTrackBinding.root).navigate(R.id.action_homeFragment_to_artistDetailFragment,bundle)
        }
        fragmentTrackBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        fragmentTrackBinding.recyclerView.adapter = trackAdapter
    }
}