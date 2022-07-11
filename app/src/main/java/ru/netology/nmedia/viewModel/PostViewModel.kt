package ru.netology.nmedia.viewModel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.SingleLiveEvent
import ru.netology.nmedia.adapter.PostListeners
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository

class PostViewModel : ViewModel(), PostListeners {
    private val repository: PostRepository = InMemoryPostRepository()
    val data by repository::data


    val currentPost = MutableLiveData<Post?>(null)
    val shareEvent = SingleLiveEvent<Post>()
    val playEvent = SingleLiveEvent<Post>()

    override fun onLikeClicked(post: Post) = repository.like(post.id)
    override fun onShareClicked(post: Post) {
        repository.share(post.id)
        shareEvent.value = post
    }

    override fun onDeleteClicked(post: Post) = repository.delete(post.id)
    override fun onEditClicked(post: Post) {
        currentPost.value = post
    }


    override fun onPlayClicked(post: Post) {
        playEvent.value = post
    }

    fun onCreateNewPost(content: String) {
        if (content.isBlank()) return
        val post = currentPost.value?.copy(
            content = content
        ) ?: Post(
            id = PostRepository.NEW_POST_ID, content = content,
            author = "Me",
            published = "now",
            video = null
        )
        repository.save(post)
        currentPost.value = null
    }

}