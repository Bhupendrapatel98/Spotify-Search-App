package com.app.growtaskapplication.ui.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.app.growtaskapplication.data.model.Item
import com.app.growtaskapplication.databinding.FragmentArtistDetailBinding
import com.app.growtaskapplication.ui.viewmodel.album.AlbumViewModel
import com.app.growtaskapplication.ui.viewmodel.artist.ArtistViewModel
import com.app.growtaskapplication.ui.viewmodel.playlist.PlaylistViewModel
import com.app.growtaskapplication.ui.viewmodel.tracks.TracksViewModel
import com.app.growtaskapplication.utills.Resource
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ArtistDetailFragment : Fragment() {
    private lateinit var fragmentArtistDetailBinding: FragmentArtistDetailBinding
    private val artistViewModel: ArtistViewModel by viewModels()
    private val albumViewModel: AlbumViewModel by viewModels()
    private val playListViewModel: PlaylistViewModel by viewModels()
    private val tracksViewModel: TracksViewModel by viewModels()
    private lateinit var type: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentArtistDetailBinding =
            FragmentArtistDetailBinding.inflate(inflater, container, false)

        arguments?.let {
            val id = it.getString("id")
            type = it.getString("type").toString()
            when (type) {
                "artist" -> {
                    artistViewModel.getArtist(id!!)
                }
                "album" -> {
                    albumViewModel.getAlbum(id!!)
                }
                "playlist" -> {
                    playListViewModel.getPlaylist(id!!)
                }
                "tracks" -> {
                    tracksViewModel.getTrack(id!!)
                }
            }
        }

        observeArtistDetail()
        observeAlbumDetail()
        observeTrackDetail()
        observePlayListDetail()

        return fragmentArtistDetailBinding.root
    }

    private fun observeTrackDetail() {
        lifecycleScope.launch {
            tracksViewModel.trackDetail.collect {
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

    private fun observePlayListDetail() {
        lifecycleScope.launch {
            playListViewModel.playlistDetail.collect {
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


    private fun observeAlbumDetail() {
        lifecycleScope.launch {
            albumViewModel.albumsDetail.collect {
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


    private fun observeArtistDetail() {
        lifecycleScope.launch {
            artistViewModel.artistDetail.collect {
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

        if (type == "tracks") {
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
            "artist" -> {
                fragmentArtistDetailBinding.artistName.text = "${item.followers?.total} followers"
                fragmentArtistDetailBinding.releaseDate.text = "${item.popularity} Popularity"
            }
            "album" -> {
                fragmentArtistDetailBinding.artistName.text = item.artists[0].name
                fragmentArtistDetailBinding.releaseDate.text = item.release_date
            }
            "playlist" -> {
                fragmentArtistDetailBinding.artistName.text = item.description
                fragmentArtistDetailBinding.releaseDate.text = item.owner!!.display_name
            }
            "tracks" -> {
                fragmentArtistDetailBinding.artistName.text = item.artists[0].name
                fragmentArtistDetailBinding.releaseDate.text = item.album!!.release_date
            }
        }

    }
}