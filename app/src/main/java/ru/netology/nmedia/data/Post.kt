package ru.netology.nmedia.data

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Post(
    @SerializedName("id")
    val id: Long,
    @SerializedName("author")
    val author: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("published")
    val published: String,
    @SerializedName("likedByMe")
    val likedByMe: Boolean = false,
    @SerializedName("likes")
    val likes: Int = 0,
    @SerializedName("shares")
    val shares: Int = 0,
    @SerializedName("views")
    val views: Int = 0,
    @SerializedName("video")
    val video: String? = null

)
