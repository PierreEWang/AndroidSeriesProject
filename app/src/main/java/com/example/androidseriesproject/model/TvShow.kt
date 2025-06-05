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
    val startDate: String? = null,

    @SerializedName("end_date")
    val endDate: String? = null,

    @SerializedName("country")
    val country: String? = null,

    @SerializedName("network")
    val network: String? = null,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("image_thumbnail_path")
    val imageThumbnailPath: String? = null
) {
    // Propriétés avec valeurs par défaut pour éviter les null
    fun getDisplayName(): String = name
    fun getDisplayNetwork(): String = network ?: "Réseau inconnu"
    fun getDisplayStatus(): String = status ?: "Statut inconnu"
    fun getDisplayCountry(): String = country ?: "Pays inconnu"
    fun getDisplayStartDate(): String = startDate ?: "Date inconnue"
    fun getImageUrl(): String = imageThumbnailPath ?: ""
}