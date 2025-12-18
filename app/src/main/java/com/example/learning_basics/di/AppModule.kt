package com.example.learning_basics.di

import android.content.Context
import androidx.room.Room
import com.example.learning_basics.data.AppDatabase
import com.example.learning_basics.data.UrlDao
import com.example.learning_basics.service.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Room
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "orufy_db"
        ).build()
    }
    // Room Provider
    @Provides
    @Singleton
    fun provideUrlDao(database: AppDatabase): UrlDao{
        return database.urlDao()
    }

    // Base URL for retrofit
    private const val BASE_URL = "https://webview.free.beeceptor.com/"

    // Retrofit
    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    // Retrofit provider
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) : ApiService{
        return retrofit.create(ApiService::class.java)
    }

}