package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.numToFormatString
import ru.netology.nmedia.*

typealias onListener = (Post) -> Unit

class PostsAdapter(
    private val onLikeClicked: onListener,
    private val onShareClicked: onListener

) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {


    class ViewHolder(
        private val binding: PostBinding,
        private val onLikeClicked: onListener,
        private val onShareClicked: onListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        init {
            binding.likeButton.setOnClickListener { onLikeClicked(post) }
            binding.shareButton.setOnClickListener { onShareClicked(post) }
        }

        fun bind(post: Post) {
            this.post = post

            with(binding) {
                authorName.text = post.author
                postMessage.text = post.content
                postDate.text = post.published
                likeButton.setImageResource(getLikeButtonImageId(post.likedByMe))
                likesAmount.text = numToFormatString(post.likes)
                sharesAmount.text = numToFormatString(post.shares)
                viewsAmount.text = numToFormatString(post.views)
            }
        }

        private fun getLikeButtonImageId(liked: Boolean) =
            if (liked) R.drawable.ic_favorite_24 else R.drawable.ic_favorite_border_24

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, onLikeClicked, onShareClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }


    private object DiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id


        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem

    }
}