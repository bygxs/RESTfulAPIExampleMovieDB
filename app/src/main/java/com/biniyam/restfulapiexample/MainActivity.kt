package com.biniyam.restfulapiexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.biniyam.restfulapiexample.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.btnGetMovie.setOnClickListener {

            val movieId = 500
            CoroutineScope(Dispatchers.IO).launch {
                val movie = getMovieDetails(movieId)
                println("movie = ${movie}")

                runOnUiThread{
                    binding.tvMovieDetails.text = movie.toString()
                }

            }
        }
    }

       suspend fun getMovieDetails(movieId: Int): MovieModel {

            val retrofit = Retrofit.Builder()
            .baseUrl(getString(R.string.BASE_URL))
            .addConverterFactory(GsonConverterFactory.create())
            .build()

            val movieAPIService = retrofit.create(MovieAPIService::class.java)
            return  movieAPIService.getMovieDetails(movieId,getString(R.string.api_key))


        }

    }
