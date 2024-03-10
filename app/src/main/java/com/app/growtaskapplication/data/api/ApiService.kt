package com.app.growtaskapplication.data.api

import com.app.growtaskapplication.data.model.Item
import com.app.growtaskapplication.data.model.SearchResponse
import com.app.growtaskapplication.data.model.token.GetTokenResponse
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("api/token")
    suspend fun getToken(
        @Field("grant_type") grantType: String,
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
    ): GetTokenResponse

    @GET("search")
    suspend fun searchAlbum(@QueryMap query: HashMap<String, String>): SearchResponse

    @GET("artists/{id}")
    suspend fun getArtist(@Path("id") artistId:String): Item

    @GET("albums/{id}")
    suspend fun getAlbum(@Path("id") albumId:String): Item

    @GET("playlists/{id}")
    suspend fun getPlayList(@Path("id") playlistId:String): Item

    @GET("tracks/{id}")
    suspend fun getTracks(@Path("id") TrackId:String): Item
}