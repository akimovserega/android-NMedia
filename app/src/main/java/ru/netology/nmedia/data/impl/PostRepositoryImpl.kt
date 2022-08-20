package ru.netology.nmedia.data.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.PostRepository.Companion.NEW_POST_ID
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.db.PostEntity
import ru.netology.nmedia.db.toEntity
import ru.netology.nmedia.db.toModel

class PostRepositoryImpl(private val dao: PostDao) : PostRepository {


    private var nextId = POSTS_AMOUNT.toLong()

    override val data = dao.getAll().map { entities ->
        entities.map {it.toModel()}
    }


    override fun like(postId: Long) {
        dao.likeById(postId)

    }

    override fun share(postId: Long) {
        dao.shareById(postId)
    }

    override fun delete(postId: Long) {
        dao.removeById(postId)
    }

    override fun save(post: Post) {
            if(post.id==NEW_POST_ID) dao.insert(post.toEntity()) else dao.updateContentById(post.id, post.content)
    }


    private companion object {
        const val POSTS_AMOUNT = 10
    }

}