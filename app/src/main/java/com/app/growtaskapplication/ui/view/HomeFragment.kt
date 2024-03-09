package com.app.growtaskapplication.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.app.growtaskapplication.R
import com.app.growtaskapplication.databinding.FragmentAlbumBinding
import com.app.growtaskapplication.databinding.FragmentHomeBinding
import com.app.growtaskapplication.ui.viewmodel.album.AlbumViewModel
import com.app.growtaskapplication.ui.viewmodel.artist.ArtistViewModel
import com.app.growtaskapplication.ui.viewmodel.playlist.PlaylistViewModel
import com.app.growtaskapplication.ui.viewmodel.token.GetTokenViewModel
import com.app.growtaskapplication.ui.viewmodel.tracks.TracksViewModel
import com.app.growtaskapplication.utills.Constants
import com.app.growtaskapplication.utills.Resource
import com.app.growtaskapplication.utills.TokenManager
import com.app.growtaskapplication.utills.TokenRefreshService
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), TokenRefreshService {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private val tokenViewModel: GetTokenViewModel by viewModels()
    private val albumViewModel: AlbumViewModel by activityViewModels()
    private val artistViewModel: ArtistViewModel by activityViewModels()
    private val playlistViewModel: PlaylistViewModel by activityViewModels()
    private val tracksViewModel: TracksViewModel by activityViewModels()

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

        callGetTokeApi()
        changeEvents()
        setUpViewPager()
        observeToken()

        return fragmentHomeBinding.root
    }

    private fun callGetTokeApi() {
        if (tokenManager.getToken() == null) {
            tokenViewModel.getToken(
                Constants.CLIENT_CREDENTIAL,
                Constants.CLIENT_ID,
                Constants.CLIENT_SECRET
            )
        }
    }

    private fun observeToken() {

        lifecycleScope.launch {
            tokenViewModel.token.collect {
                when (it) {
                    is Resource.Loading -> {
                        //Toast.makeText(context, "Load", Toast.LENGTH_SHORT).show();
                    }
                    is Resource.Success -> {
                        tokenManager.saveToken(it.data.access_token)
                    }
                    is Resource.Failed -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun changeEvents() {
        fragmentHomeBinding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(str: Editable?) {
                if (str.toString().isNotEmpty()) {
                    albumViewModel.searchFlowQuery.value = str.toString()
                    artistViewModel.searchFlowQuery.value = str.toString()
                    tracksViewModel.searchFlowQuery.value = str.toString()
                    playlistViewModel.searchFlowQuery.value = str.toString()
                }
            }
        })
    }

    private fun setUpViewPager() {
        val viewPagerAdapter = ViewPagerAdapter(requireActivity())
        fragmentHomeBinding.viewpager.adapter = viewPagerAdapter

        TabLayoutMediator(
            fragmentHomeBinding.tabs,
            fragmentHomeBinding.viewpager
        ) { tab, position ->
            when (position) {
                0 -> tab.text = "Artist"
                1 -> tab.text = "Album"
                2 -> tab.text = "Playlist"
                3 -> tab.text = "Track"
            }
        }.attach()
    }

    override fun refreshToken() {
        tokenManager.clearData()
        callGetTokeApi()
    }


}