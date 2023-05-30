package com.example.newsapp.network

import com.example.newsapp.NewsItem
import com.example.newsapp.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("everything")
    suspend fun getNews(
        @Query("q") keyword: String,
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>
}