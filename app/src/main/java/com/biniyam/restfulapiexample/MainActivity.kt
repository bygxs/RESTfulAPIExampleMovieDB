package com.biniyam.restfulapiexample

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.biniyam.restfulapiexample.databinding.ActivityMainBinding
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var imageBaseUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.IO).launch {
            imageBaseUrl = getImageBasUrlFromConfig()
            println("imageBaseUrl = ${imageBaseUrl}")
        }


        binding.btnGetMovie.setOnClickListener {
            val movieId = 550
            val query = binding.etQuery.text.toString()

            CoroutineScope(Dispatchers.Main).launch {
                //val movie = getMovieDetails(movieId)
                // println("movie = ${movie}")

                val movie = getMovieFromSearch(query)

                loadImage(movie.imagePath)
                binding.tvMovieDetails.text = movie.overview
                binding.tvTitle.text = movie.title

            }
        }
    }

    suspend fun getMovieDetails(movieId: Int): MovieModel {

        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.BASE_URL))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val movieAPIService = retrofit.create(MovieAPIService::class.java)
        return movieAPIService.getMovieDetails(movieId, getString(R.string.api_key))

    }

    suspend fun getImageBasUrlFromConfig(): String {
        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.BASE_URL))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val movieAPIService = retrofit.create(MovieAPIService::class.java)

        val response = movieAPIService.getConfigAaJson(getString(R.string.api_key)).body()

        val imageBaseUrl = response?.getAsJsonObject("images")?.get("secure_base_url")?.asString

        val imageSize = response
            ?.getAsJsonObject("images")
            ?.getAsJsonArray("poster_sizes")
            ?.get(4)?.asString
        println(imageBaseUrl + imageSize)
        return imageBaseUrl + imageSize

    }

    fun loadImage(imageUrl: String) {
        var image: Bitmap? = null
        Thread {
            val imageUrl = imageBaseUrl + imageUrl

            try {
                val inStream = java.net.URL(imageUrl).openStream()
                image = BitmapFactory.decodeStream(inStream)
                runOnUiThread {
                    binding.ivPoster.setImageBitmap(image)
                }

            } catch (e: Exception) {
                println("Error Loading image")
                e.printStackTrace()
            }

        }.start()
    }

    suspend fun getMovieFromSearch(query: String): MovieModel {
        val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.BASE_URL))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val movieAPIService = retrofit.create(MovieAPIService::class.java)

        val response = movieAPIService.searchByTitle(getString(R.string.api_key), query)
            .body() //.code(),.header()
        println("movieJson = ${response}")
        val movieJson = response?.getAsJsonArray("results")?.get(0)
        println("movieJson = ${movieJson}")

        val gson = Gson()
        val movie = gson.fromJson(movieJson, MovieModel::class.java)
        println("movie objekt = ${movie}")

        return movie

    }

}
