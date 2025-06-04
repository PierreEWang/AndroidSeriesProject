package com.example.androidseriesproject.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing a TV show from the EpisoDate API
 */


data class TvShow(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("permalink")
    val permalink: String,
    
    @SerializedName("start_date")
    val startDate: String,
    
    @SerializedName("end_date")
    val endDate: String?,
    
    @SerializedName("country")
    val country: String,
    
    @SerializedName("network")
    val network: String,
    
    @SerializedName("status")
    val status: String,
    
    @SerializedName("image_thumbnail_path")
    val imageThumbnailPath: String
)

