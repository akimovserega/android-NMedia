package ru.netology.nmedia.data.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.R
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.data.PostRepository

class InMemoryPostRepository : PostRepository {
    override val data = MutableLiveData(
        Post(
            id = 1L,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология!" +
                    " Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу." +
                    " Затем появились курсы по дизайну, разработке, аналитике и управлению." +
                    " Мы растем сами и помогаем расти студентам: от новичков до уверенных профессионалов." +
                    " Но самое важное остается с нами: мы верим, что в каждом есть сила, которая заставляет" +
                    " хотеть больше, целиться выше, бежать быстрее. Наша миссия - помочь встать" +
                    " на путь роста и начать цепочку перемен \u2192  http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likes = 999,
            shares = 9998,
            views = 1_100_000
        )

    )


    override fun like() {
        val currentPost = checkNotNull(data.value) {
            "Data value is null. Incorrect"
        }

        val likedPost = currentPost.copy(
            likedByMe = !currentPost.likedByMe,
            likes = if (currentPost.likedByMe) currentPost.likes - 1 else currentPost.likes + 1
        )
        data.value = likedPost
    }

    override fun share() {
        val currentPost = checkNotNull(data.value) {
            "Data value is null. Incorrect"
        }
        data.value = currentPost.copy(shares = currentPost.shares + 1)
    }
}