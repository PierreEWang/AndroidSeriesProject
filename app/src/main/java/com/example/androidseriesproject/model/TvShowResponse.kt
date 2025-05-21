package com.example.androidseriesproject.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the response from the EpisoDate API's most-popular endpoint
 */
data class TvShowResponse(
    @SerializedName("total")
    val total: Int,
    
    @SerializedName("page")
    val page: Int,
    
    @SerializedName("pages")
    val pages: Int,
    
    @SerializedName("tv_shows")
    val tvShows: List<TvShow>
)