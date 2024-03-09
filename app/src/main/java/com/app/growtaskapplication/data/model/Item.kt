package com.app.growtaskapplication.data.model

import com.app.growtaskapplication.data.model.album.Artist
import com.app.growtaskapplication.data.model.tracks.Album
import com.app.growtaskapplication.data.model.tracks.ExternalIds

data class Item(val album_type: String,
                val artists: List<Artist>,
                val available_markets: List<String>,
                val external_urls: ExternalUrlsX,
                val href: String,
                val id: String,
                val images: List<Image>,
                val name: String,
                val release_date: String,
                val release_date_precision: String,
                val total_tracks: Int,
                val type: String,
                val followers: Followers,
                val genres: List<String>,
                val popularity: Int,
                val uri: String,
                val collaborative: Boolean,
                val description: String,
                val owner: Owner,
                val primary_color: Any,
                val `public`: Any,
                val snapshot_id: String,
                val tracks: Tracks,
                val album: Album,
                val disc_number: Int,
                val duration_ms: Int,
                val explicit: Boolean,
                val external_ids: ExternalIds,
                val is_local: Boolean,
                val preview_url: String,
                val track_number: Int
                )
