package com.example.newsapp.di

import com.example.newsapp.data.remote.api.NewsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module         // This is a Hilt Module
@InstallIn(SingletonComponent::class)       // Install in the application-level component
object NetworkModule {

    @Provides                           //This function provides dependency
    @Singleton                          // There should only be 1 instance of Retrofit
    fun provideRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl("https://newsapi.org/")                        // Base URL of the API
            .addConverterFactory(GsonConverterFactory.create())            // use GSON for JSON conversion
            .build()
    }

    @Provides
    @Singleton
    fun provideNewsApi(retrofit: Retrofit): NewsApi{    // Retrofit uses the provideRetrofit instance to create the NewsApi implementation
        return retrofit.create(NewsApi::class.java)
    }
}