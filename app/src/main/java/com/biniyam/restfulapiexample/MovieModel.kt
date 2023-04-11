package com.biniyam.restfulapiexample

import com.google.gson.annotations.SerializedName

data class MovieModel (
    val id: Int,
    val title: String,
   @SerializedName("release_date") val releaseDate:String,
    val overview: String,
    @SerializedName("poster_path")val imagePath: String

    ) {
}