package com.app.growtaskapplication.data.model.album

import com.app.growtaskapplication.data.model.ExternalUrlsX
import com.app.growtaskapplication.data.model.Item

data class SearchAlbumResponse(val albums: Albums)
data class Albums(
    val href: String,
    val items: List<Item>,
    val limit: Int,
    val next: String,
    val offset: Int,
    val previous: Any,
    val total: Int
)

data class Artist(
    val external_urls: ExternalUrlsX,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
)

