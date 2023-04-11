package com.biniyam.restfulapiexample

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieAPIService {

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id")id: Int,
        @Query("api_key")apiKey: String) : MovieModel // Response<>


}