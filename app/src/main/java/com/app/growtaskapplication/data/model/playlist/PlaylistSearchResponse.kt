package com.app.growtaskapplication.data.model.playlist

import com.app.growtaskapplication.data.model.Item

data class PlaylistSearchResponse(val playlists: Playlists)
data class Playlists(
    val href: String,
    val items: List<Item>,
    val limit: Int,
    val next: String,
    val offset: Int,
    val previous: String,
    val total: Int
)

