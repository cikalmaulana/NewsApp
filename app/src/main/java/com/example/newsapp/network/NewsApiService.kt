package com.example.newsapp.network

import com.example.newsapp.NewsItem
import com.example.newsapp.NewsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {

//    https://newsapi.org/v2/everything?q=keyword&apiKey=f84d5717585f4b6a9a4ba3acab6d1ce5
    @GET("everything")
    suspend fun getAllNews(
        @Query("q") keyword: String,
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>

//    https://newsapi.org/v2/top-headlines?country=id&apiKey=f84d5717585f4b6a9a4ba3acab6d1ce5
    @GET("top-headlines")
    suspend fun getNewsByCountry(
        @Query("country") keyword: String,
        @Query("apiKey") apiKey: String
    ): Response<NewsResponse>
}