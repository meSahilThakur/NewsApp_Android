package com.example.newsapp.data.remote.api

import com.example.newsapp.BuildConfig
import com.example.newsapp.data.remote.dto.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country")
        country: String = "us",         // Example: get US headlines
        @Query("apiKey")
        apiKey: String = "2ec3c5d80e13469ebe4b9d1d0bc43aee"   //BuildConfig.NEWS_API_KEY       // actual API key
    ): NewsResponse

    // we should add more functions for searching, etc.
     @GET("v2/everything")
     suspend fun searchNews(
         @Query("q") query: String,
         @Query("apiKey") apiKey: String = "2ec3c5d80e13469ebe4b9d1d0bc43aee"
     ): NewsResponse
}