package ru.netology.nmedia.service

import com.google.gson.annotations.SerializedName

data class Like(
    @SerializedName("userId")
    val userId: Long,

    @SerializedName("userName")
    val userName: String,

    @SerializedName("postId")
    val postId: String,

    @SerializedName("postAuthor")
    val postAuthor: String
) {
}