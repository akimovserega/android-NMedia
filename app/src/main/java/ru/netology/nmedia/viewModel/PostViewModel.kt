package ru.netology.nmedia.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.adapter.PostListeners
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository

class PostViewModel : ViewModel(), PostListeners {
    private val repository: PostRepository = InMemoryPostRepository()
    val data by repository::data

    val currentPost = MutableLiveData<Post?>(null)

    override fun onLikeClicked(post: Post) = repository.like(post.id)
    override fun onShareClicked(post: Post) = repository.share(post.id)
    override fun onDeleteClicked(post: Post) = repository.delete(post.id)
    override fun onEditClicked(post: Post) {
        currentPost.value = post
    }


    fun onSaveClicked(content: String) {
        if (content.isBlank()) return
        val post = currentPost.value?.copy(
            content = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID, content = content,
            author = "Me",
            published = "now"
        )
        repository.save(post)
        currentPost.value = null
    }

    fun onCancelClicked() {
        currentPost.value = null
    }
}