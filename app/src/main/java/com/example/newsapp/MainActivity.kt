package com.example.newsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.network.NewsApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    companion object {
        private const val API_KEY = "f84d5717585f4b6a9a4ba3acab6d1ce5"
        private const val KEYWORD = "keyword"
        private const val COUNTRY = "id"
    }
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://newsapi.org/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val newsApiService = retrofit.create(NewsApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutManager = LinearLayoutManager(this)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvNews.layoutManager = layoutManager

        fetchDataFromApi(binding)
    }

    private fun fetchDataFromApi(binding: ActivityMainBinding) {
        CoroutineScope(Dispatchers.IO).launch {
            val response = newsApiService.getAllNews(KEYWORD, API_KEY)
            if (response.isSuccessful) {
                val newsResponse = response.body()
                val newsList = ArrayList(newsResponse?.articles ?: emptyList())
                withContext(Dispatchers.Main) {
                    binding.rvNews.adapter = NewsAdapter(newsList)
                    Glide.with(binding.root)
                        .load(newsList.get(0).urlToImage)
                        .into(binding.ivHeadline)
                    binding.tvHeadline.text = newsList.get(0).title
                    binding.tvHeadlineAuthor.text = newsList.get(0).author
                    binding.tvHeadlineDate.text = changeDateFormat(newsList.get(0).publishedAt)
                }
            } else {
                val error = response.errorBody()?.string()
                Log.d("HIT_NEWS", "Gagal Fetch Data")
            }
        }
    }

    fun changeDateFormat(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("d MMM, yyyy", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }
}