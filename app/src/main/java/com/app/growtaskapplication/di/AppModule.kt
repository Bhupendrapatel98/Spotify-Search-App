package com.app.growtaskapplication.di

import android.app.Application
import androidx.room.Room
import com.app.growtaskapplication.data.api.ApiService
import com.app.growtaskapplication.data.interceptor.HeaderInterceptor
import com.app.growtaskapplication.data.local.SearchDatabase
import com.app.growtaskapplication.ui.view.HomeFragment
import com.app.growtaskapplication.utills.TokenRefreshService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Named("tokenUrl")
    fun provideBaseUrlToken(): String = "https://accounts.spotify.com/"

    @Provides
    @Named("albumUrl")
    fun provideBaseUrl(): String = "https://api.spotify.com/v1/"

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor():HttpLoggingInterceptor{
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(headerInterceptor: HeaderInterceptor, httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(headerInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Provides
    @Named("tokenBuilder")
    fun provideRetrofitBuilderToken(@Named("tokenUrl") baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Provides
    @Named("albumBuilder")
    fun provideRetrofitBuilder(
        @Named("albumUrl") baseUrl: String,
        okHttpClient: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Named("tokenService")
    fun provideApiServiceToken(@Named("tokenBuilder") retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Named("commonService")
    fun provideApiService(@Named("albumBuilder") retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    fun provideTokenRefreshService():TokenRefreshService {
        return HomeFragment()
    }

    @Provides
    @Singleton
    fun provideDatabase(app: Application) : SearchDatabase =
        Room.databaseBuilder(app, SearchDatabase::class.java, "search_database")
            .fallbackToDestructiveMigration()
            .build()
}