package ru.netology.nmedia.viewModel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.SingleLiveEvent
import ru.netology.nmedia.adapter.PostListeners
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.data.PostRepository
import ru.netology.nmedia.data.impl.FilePostRepository
import ru.netology.nmedia.data.impl.InMemoryPostRepository
import ru.netology.nmedia.data.impl.SharedPrefPostRepository

class PostViewModel(application: Application) : AndroidViewModel(application), PostListeners {
    private val repository: PostRepository = FilePostRepository(application)
    val data by repository::data


    private val currentPost = MutableLiveData<Post?>(null)
    val shareEvent = SingleLiveEvent<Post>()
    val playEvent = SingleLiveEvent<Post>()
    val tapEvent = SingleLiveEvent<Long>()
    val addEvent = SingleLiveEvent<String?>()


    override fun onLikeClicked(post: Post) = repository.like(post.id)
    override fun onShareClicked(post: Post) {
        repository.share(post.id)
        shareEvent.value = post
    }

    override fun onDeleteClicked(post: Post) = repository.delete(post.id)
    override fun onEditClicked(post: Post) {
        currentPost.value = post
        addEvent.value = post.content
    }


    override fun onPlayClicked(post: Post) {
        playEvent.value = post
    }

    override fun onPostClicked(post: Post) {
        tapEvent.value = post.id
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

    fun onAddClicked() {
        addEvent.value = null
    }

}