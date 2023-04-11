package com.biniyam.restfulapiexample

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieAPIService {

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id")id: Int,
        @Query("api_key")apiKey: String) : MovieModel // Response<>

    @GET("configuration")
    suspend fun getConfigAaJson(
        @Query("api_key") query: String
    ): Response<JsonObject>

    @GET("search/movie")
    suspend fun searchByTitle(
       @Query("api_key") apiKey: String,
        @Query("query") query: String
    ): Response<JsonObject>


}