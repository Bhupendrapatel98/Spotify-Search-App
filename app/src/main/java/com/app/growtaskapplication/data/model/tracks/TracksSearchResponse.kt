package com.app.growtaskapplication.data.model.tracks

import com.app.growtaskapplication.data.model.Image
import com.app.growtaskapplication.data.model.Item

data class TracksSearchResponse(
    val tracks: Tracks
)

data class Tracks(
    val href: String,
    val items: List<Item>,
    val limit: Int,
    val next: String,
    val offset: Int,
    val previous: String,
    val total: Int
)

data class ExternalIds(
    val isrc: String
)

data class ExternalUrlsXXX(
    val spotify: String
)

data class Album(
    val album_type: String,
    val artists: List<ArtistX>,
    val available_markets: List<String>,
    val external_urls: ExternalUrlsXXX,
    val href: String,
    val id: String,
    val images: List<Image>,
    val name: String,
    val release_date: String,
    val release_date_precision: String,
    val total_tracks: Int,
    val type: String,
    val uri: String
)

data class ArtistX(
    val external_urls: ExternalUrlsXXX,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)

