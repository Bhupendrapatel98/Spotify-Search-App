package com.app.growtaskapplication.data.model.artist

import com.app.growtaskapplication.data.model.Item

data class ArtistSearchResponse(val artists: Artists)
data class Artists(
    val href: String,
    val items: List<Item>,
    val limit: Int,
    val next: String,
    val offset: Int,
    val previous: Any,
    val total: Int)