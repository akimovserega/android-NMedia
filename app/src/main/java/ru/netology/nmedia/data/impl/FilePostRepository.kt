package ru.netology.nmedia.data.impl

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.data.PostRepository
import kotlin.properties.Delegates

class FilePostRepository(private val application: Application) : PostRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type

    private val prefs = application.getSharedPreferences(
        "repo", Context.MODE_PRIVATE
    )

    private var nextId by Delegates.observable(
        prefs.getLong(POSTS_NEXT_ID_PREFS_KEY, 10L)

    ) { _, _, newValue ->
        prefs.edit { putLong(POSTS_NEXT_ID_PREFS_KEY, newValue) }

    }

    private var posts
        get() = checkNotNull(data.value)
        set(value) {

            application.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).bufferedWriter().use {
                it.write(gson.toJson(value))
            }
            data.value = value
        }

    override val data: MutableLiveData<List<Post>>

    init {

        val examplePosts = listOf(
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
                views = 1_100_000,
                likedByMe = true,
                video = "https://www.youtube.com/watch?v=WhWc3b3KhnY"
            ),
            Post(
                id = 2L,
                author = "Нетология. Университет интернет-профессий будущего",
                content = "Мы начинали с курсов об онлайн-маркетинге. Затем выросли в университет " +
                        "интернет-профессий: учили дизайнеров, аналитиков, программистов, менеджеров, " +
                        "маркетологов… Но обучать новым профессиям — это не предел. " +
                        "Мы продолжаем расти \u2192  http://netolo.gy/fyb",
                published = "22 мая в 18:36",
                likes = 0,
                shares = 101,
                views = 1_100
            ),
            Post(
                id = 3L,
                author = "Нетология. Университет интернет-профессий будущего",
                content = "Сегодня мы даём знания не только начинающим, но и тем, кто давно в профессии. " +
                        "Специалисты изучают новые инструменты, топ-менеджеры — получают степень MBA, руководители " +
                        "бизнеса — обучают своих сотрудников и обучаются сами" +
                        " \u2192  http://netolo.gy/fyb",
                published = "23 мая в 18:36",
                likes = 1,
                shares = 0,
                views = 1
            ),
            Post(
                id = 4L,
                author = "Нетология. Университет интернет-профессий будущего",
                content = "Мы знаем, что в каждом из нас уже есть та внутренняя сила, " +
                        "которая заставляет всегда хотеть больше, целиться выше, бежать быстрее. " +
                        "Наша миссия — помочь встать на путь роста и пройти с человеком по этому " +
                        "пути как можно дальше. Сделать так, чтобы желание перемен стало сильнее страха " +
                        "перемен. Обучить новому, влюбить в знания. Стать импульсом к действию." +
                        " \u2192  http://netolo.gy/fyb",
                published = "24 мая в 18:36",
                likes = 999,
                shares = 999,
                views = 100_000
            )

        )
        val postsFile = application.filesDir.resolve(FILE_NAME)
        val posts: List<Post> = if (postsFile.exists()) {
            val inputStream = application.openFileInput(FILE_NAME)
            val reader = inputStream.bufferedReader()
            reader.use {
                gson.fromJson(it, type)
            }
        } else examplePosts

        val lastId = posts.maxByOrNull { it.id }?.id
        if (lastId != null) {
            if (lastId > nextId) nextId = lastId
        }


        data = MutableLiveData(posts)

    }


    override fun like(postId: Long) {
        posts = posts.map { post ->
            if (post.id == postId) post.copy(
                likedByMe = !post.likedByMe,
                likes = if (post.likedByMe) post.likes - 1 else post.likes + 1
            )
            else post

        }
    }

    override fun share(postId: Long) {
        posts = posts.map { post ->
            if (post.id == postId) post.copy(shares = post.shares + 1)
            else post
        }

    }

    override fun delete(postId: Long) {
        posts = posts.filterNot {
            it.id == postId
        }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)

    }

    private fun update(post: Post) {
        posts = posts.map {
            if (it.id == post.id) post else it
        }
    }

    private fun insert(post: Post) {
        posts = listOf(post.copy(id = ++nextId)) + posts
    }

    private companion object {
        const val POSTS_NEXT_ID_PREFS_KEY = "posts"
        const val FILE_NAME = "posts.Json"
    }
}