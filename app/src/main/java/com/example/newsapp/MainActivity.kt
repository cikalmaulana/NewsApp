package com.example.newsapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.newsapp.databinding.ActivityMainBinding
import com.example.newsapp.network.NewsApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var loadingSpinner: ProgressBar
    private lateinit var cv_icon1: ConstraintLayout

    companion object {
        private const val API_KEY = "f84d5717585f4b6a9a4ba3acab6d1ce5"
        private const val KEYWORD = "keyword"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layoutManager = LinearLayoutManager(this)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingSpinner = binding.loadingSpinner
        binding.rvNews.layoutManager = layoutManager

        cv_icon1 = findViewById(R.id.cv_icon1)

        fetchDataFromApi(binding)
    }

    private fun fetchDataFromApi(binding: ActivityMainBinding) {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                loadingSpinner.visibility = View.VISIBLE
            }
            val newsApiService: NewsApiService = ApiClient.getClient().create(NewsApiService::class.java)
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
                    binding.cvIcon1.setOnClickListener{
                        openUrl(newsList.get(0).url)
                    }
                }
            } else {
                val error = response.errorBody()?.string()
                Log.d("HIT_NEWS", "Gagal Fetch Data")
                withContext(Dispatchers.Main) {
                    loadingSpinner.visibility = View.GONE
                }
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@MainActivity,
                        "Gagal Mengambil Data",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            withContext(Dispatchers.Main) {
                loadingSpinner.visibility = View.GONE
            }
        }
    }

    fun changeDateFormat(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("d MMM, yyyy", Locale.getDefault())

        val date = inputFormat.parse(inputDate)
        return outputFormat.format(date)
    }

    private fun openUrl(url: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }
}