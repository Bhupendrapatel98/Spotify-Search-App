package com.app.growtaskapplication.data.local

import androidx.room.TypeConverter
import com.app.growtaskapplication.data.model.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromOwnerJson(json: String?): Owner? {
        return if(json == null) null else Gson().fromJson(json, Owner::class.java)
    }

    @TypeConverter
    fun toOwnerJson(owner: Owner?): String? {
        return owner?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun fromAlbumJson(json: String?): Album? {
        return if (json == null) null else Gson().fromJson(json, Album::class.java)
    }

    @TypeConverter
    fun toAlbumJson(album: Album?): String? {
        return album?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun fromFollowerJson(json: String?): Followers? {
        return if (json == null) null else Gson().fromJson(json, Followers::class.java)
    }

    @TypeConverter
    fun toFollowerJson(followers: Followers?): String? {
        return followers?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun fromJson(json: String): List<Artist> {
        if (json == null) {
            return emptyList()
        }
        val type = object : TypeToken<List<Artist>>() {}.type
        return Gson().fromJson(json, type)?: emptyList()
    }

    @TypeConverter
    fun toJson(artists: List<Artist>?): String {
        return if (artists != null) {
            Gson().toJson(artists)
        } else {
            ""
        }
    }

    @TypeConverter
    fun fromExternalUrlsXToJson(externalUrlsX: ExternalUrlsX?): String? {
        return gson.toJson(externalUrlsX)
    }

    @TypeConverter
    fun fromJsonToExternalUrlsX(json: String?): ExternalUrlsX? {
        return gson.fromJson(json, ExternalUrlsX::class.java)
    }

    @TypeConverter
    fun fromImageListToJson(images: List<Image>?): String? {
        return gson.toJson(images)
    }

    @TypeConverter
    fun fromJsonToImageList(json: String?): List<Image>? {
        val type = object : TypeToken<List<Image>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromExternalUrlsXXXToJson(externalUrlsXXX: ExternalUrlsXXX?): String? {
        return gson.toJson(externalUrlsXXX)
    }

    @TypeConverter
    fun fromJsonToExternalUrlsXXX(json: String?): ExternalUrlsXXX? {
        return gson.fromJson(json, ExternalUrlsXXX::class.java)
    }

    @TypeConverter
    fun fromArtistXListToJson(artists: List<ArtistX>?): String? {
        return gson.toJson(artists)
    }

    @TypeConverter
    fun fromJsonToArtistXList(json: String?): List<ArtistX>? {
        val type = object : TypeToken<List<ArtistX>>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun fromExternalUrlsToJson(externalUrls: ExternalUrls?): String? {
        return gson.toJson(externalUrls)
    }

    @TypeConverter
    fun fromJsonToExternalUrls(json: String?): ExternalUrls? {
        return gson.fromJson(json, ExternalUrls::class.java)
    }

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.joinToString(separator = ",")
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.split(",")?.map { it.trim() }
    }
}