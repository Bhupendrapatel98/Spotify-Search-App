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
import com.app.growtaskapplication.databinding.FragmentTrackBinding
import com.app.growtaskapplication.ui.view.adapter.SearchItemAdapter
import com.app.growtaskapplication.ui.viewmodel.SearchUserViewModel
import com.app.growtaskapplication.utills.Resource
import com.app.growtaskapplication.utills.UserType
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

class TrackFragment : Fragment() {

    private lateinit var fragmentTrackBinding: FragmentTrackBinding
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
                .collect {
                    searchUserViewModel.search(UserType.TRACKS)
                }
        }

        setUpAdapter()
        attachObservers()

        return fragmentTrackBinding.root
    }

    private fun setUpAdapter() {

        trackAdapter = SearchItemAdapter(tracksList,UserType.TRACKS){ type, id->
            val bundle = Bundle()
            bundle.putString("type", type.type)
            bundle.putString("id", id)
            Navigation.findNavController(fragmentTrackBinding.root).navigate(R.id.action_homeFragment_to_artistDetailFragment,bundle)
        }
        fragmentTrackBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        fragmentTrackBinding.recyclerView.adapter = trackAdapter
    }


    private fun attachObservers() {
        lifecycleScope.launch {
            searchUserViewModel.track.collect {
                when (it) {
                    is Resource.Loading -> {
                        //show Loader
                    }
                    is Resource.Failed -> {
                        //handle failure
                    }
                    is Resource.Success -> {
                        val dataFiltered = it.data.tracks?.items?.filter { item ->
                            item.type == "track"
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

    private fun handleSuccess(data: List<Item>?) {
        tracksList.clear()
        data?.let { tracksList.addAll(it) }
        trackAdapter.notifyDataSetChanged()
    }
}