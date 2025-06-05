package com.example.androidseriesproject.model

import com.google.gson.annotations.SerializedName

/**
 * Data class representing detailed information about a TV show from the EpisoDate API
 */
data class TvShowDetails(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("permalink")
    val permalink: String? = null,

    @SerializedName("url")
    val url: String? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("description_source")
    val descriptionSource: String? = null,

    @SerializedName("start_date")
    val startDate: String? = null,

    @SerializedName("end_date")
    val endDate: String? = null,

    @SerializedName("country")
    val country: String? = null,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("runtime")
    val runtime: Int? = null,

    @SerializedName("network")
    val network: String? = null,

    @SerializedName("youtube_link")
    val youtubeLink: String? = null,

    @SerializedName("image_path")
    val imagePath: String? = null,

    @SerializedName("image_thumbnail_path")
    val imageThumbnailPath: String? = null,

    @SerializedName("rating")
    val rating: String? = null,

    @SerializedName("rating_count")
    val ratingCount: String? = null,

    @SerializedName("countdown")
    val countdown: Any? = null,

    @SerializedName("genres")
    val genres: List<String>? = null,

    @SerializedName("pictures")
    val pictures: List<String>? = null,

    @SerializedName("episodes")
    val episodes: List<Episode>? = null
) {
    // Méthodes helper pour éviter les null
    fun getDisplayName(): String = name
    fun getDisplayDescription(): String = description ?: "Aucune description disponible"
    fun getDisplayNetwork(): String = network ?: "Réseau inconnu"
    fun getDisplayCountry(): String = country ?: "Pays inconnu"
    fun getDisplayStatus(): String = status ?: "Statut inconnu"
    fun getDisplayStartDate(): String = startDate ?: "Date inconnue"
    fun getDisplayEndDate(): String = endDate ?: "En cours"
    fun getDisplayRuntime(): String = runtime?.let { "${it} minutes" } ?: "Durée inconnue"
    fun getDisplayRating(): String = rating ?: ""
    fun getDisplayRatingCount(): String = ratingCount ?: ""
    fun getDisplayGenres(): List<String> = genres ?: emptyList()
    fun getDisplayPictures(): List<String> = pictures ?: emptyList()
    fun getImageUrl(): String = imagePath ?: imageThumbnailPath ?: ""
    fun hasRating(): Boolean = !rating.isNullOrEmpty() && rating != "0"
    fun hasEndDate(): Boolean = !endDate.isNullOrEmpty()
    fun hasDescription(): Boolean = !description.isNullOrEmpty()
}

/**
 * Data class representing an episode
 */
data class Episode(
    @SerializedName("season")
    val season: Int,

    @SerializedName("episode")
    val episode: Int,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("air_date")
    val airDate: String? = null
) {
    fun getDisplayName(): String = name ?: "Épisode sans titre"
    fun getDisplayAirDate(): String = airDate?.substring(0, 10) ?: "Date inconnue" // Format YYYY-MM-DD
}