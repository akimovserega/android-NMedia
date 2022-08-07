package ru.netology.nmedia.db

import android.database.Cursor
import ru.netology.nmedia.data.Post

internal fun PostEntity.toModel() = Post(

    id = id,
    author = author,
    content = content,
    published = published,
    likedByMe = likedByMe,
    likes = likes,
    shares = shares,
    views = views,
    video = video

)

internal fun Post.toEntity() = PostEntity(

    id = id,
    author = author,
    content = content,
    published = published,
    likedByMe = likedByMe,
    likes = likes,
    shares = shares,
    views = views,
    video = video

)