package com.app.growtaskapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "searchResults")
data class Item(
    @PrimaryKey val id: String,
    val album_type: String?,
    val artists: List<Artist>?,
    val images: List<Image>?,
    val name: String?,
    val release_date: String?,
    val release_date_precision: String?,
    val total_tracks: Int?,
    val type: String?,
    val followers: Followers?,
    val popularity: Int?,
    val uri: String?,
    val collaborative: Boolean,
    val description: String?,
    val owner: Owner?,
    val album: Album?,
    val disc_number: Int?,
    val duration_ms: Int?,
    val explicit: Boolean,
    val is_local: Boolean,
    val preview_url: String?,
    val track_number: Int?
)
