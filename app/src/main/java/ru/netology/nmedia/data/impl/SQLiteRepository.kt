package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.db.PostDao

class SQLiteRepository(private val dao: PostDao) : PostRepository {
    private var posts
        get() = checkNotNull(data.value)
        set(value) {

            data.value = value
        }

    private var nextId = POSTS_AMOUNT.toLong()

    override val data = MutableLiveData(dao.getAll())


    override fun like(postId: Long) {
        dao.likeById(postId)
        posts = posts.map { post ->
            if (post.id == postId) post.copy(
                likedByMe = !post.likedByMe,
                likes = if (post.likedByMe) post.likes - 1 else post.likes + 1
            )
            else post

        }
    }

    override fun share(postId: Long) {
        dao.shareById(postId)
        posts = posts.map { post ->
            if (post.id == postId) post.copy(shares = post.shares + 1)
            else post
        }

    }

    override fun delete(postId: Long) {
        dao.removeById(postId)
        posts = posts.filterNot {
            it.id == postId
        }
    }

    override fun save(post: Post) {
        dao.save(post)
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
        const val POSTS_AMOUNT = 10
    }

}