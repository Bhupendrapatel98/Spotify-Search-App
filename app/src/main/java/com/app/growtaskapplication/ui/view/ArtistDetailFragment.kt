package com.app.growtaskapplication.ui.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.app.growtaskapplication.data.model.Item
import com.app.growtaskapplication.databinding.FragmentArtistDetailBinding
import com.app.growtaskapplication.ui.viewmodel.UserDetailViewModel
import com.app.growtaskapplication.utills.Resource
import com.app.growtaskapplication.utills.UserType
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArtistDetailFragment : Fragment() {
    private lateinit var fragmentArtistDetailBinding: FragmentArtistDetailBinding
    private val userDetailViewModel: UserDetailViewModel by viewModels()
    private lateinit var type: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        fragmentArtistDetailBinding = FragmentArtistDetailBinding.inflate(inflater, container, false)

        arguments?.let { it ->
            val id = it.getString("id")
            type = it.getString("type").toString()
            userDetailViewModel.getUserDetail(id!!, type)
        }

        observeUserDetail()

        return fragmentArtistDetailBinding.root
    }

    private fun observeUserDetail() {
        lifecycleScope.launch {
            userDetailViewModel.userDetail.collect {
                when (it) {
                    is Resource.Loading -> {
                    }
                    is Resource.Failed -> {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Success -> {
                        handleSuccessResponse(it.data)
                    }

                    else -> {}
                }
            }
        }
    }

    private fun handleSuccessResponse(item: Item) {

        if (type == UserType.TRACKS.type) {
            if (item.album!!.images.isNotEmpty()) {
                Glide.with(context!!).load(item.album!!.images[0].url)
                    .into(fragmentArtistDetailBinding.image)
            }
        } else {
            if (item.images.isNotEmpty()) {
                Glide.with(context!!).load(item.images[0].url)
                    .into(fragmentArtistDetailBinding.image)
            }
        }

        fragmentArtistDetailBinding.name.text = item.name

        when (type) {
            UserType.ARTIST.type -> {
                fragmentArtistDetailBinding.artistName.text = "${item.followers?.total} followers"
                fragmentArtistDetailBinding.releaseDate.text = "${item.popularity} Popularity"
            }
            UserType.ALBUM.type -> {
                fragmentArtistDetailBinding.artistName.text = item.artists[0].name
                fragmentArtistDetailBinding.releaseDate.text = item.release_date
            }
            UserType.PLAYLIST.type -> {
                fragmentArtistDetailBinding.artistName.text = item.description
                fragmentArtistDetailBinding.releaseDate.text = item.owner!!.display_name
            }
            UserType.TRACKS.type -> {
                fragmentArtistDetailBinding.artistName.text = item.artists[0].name
                fragmentArtistDetailBinding.releaseDate.text = item.album!!.release_date
            }
        }

    }
}