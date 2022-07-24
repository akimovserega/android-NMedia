package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.numToFormatString
import ru.netology.nmedia.*

class PostsAdapter(
    private val interactionListeners: PostListeners

) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {


    class ViewHolder(
        private val binding: PostBinding,
        listener: PostListeners

    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post
        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.optionButton).apply {
                inflate(R.menu.option)
                setOnMenuItemClickListener { menuItem ->
                    when (menuItem.itemId) {
                        R.id.remove -> {
                            listener.onDeleteClicked(post)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditClicked(post)
                            true
                        }
                        else -> false
                    }
                }
            }
        }


        init {
            binding.likeButton.setOnClickListener { listener.onLikeClicked(post) }
            binding.shareButton.setOnClickListener { listener.onShareClicked(post) }
            binding.videoScreen.setOnClickListener { listener.onPlayClicked(post) }
            binding.playButton.setOnClickListener { listener.onPlayClicked(post) }
            binding.postMessage.setOnClickListener { listener.onPostClicked(post) }
        }

        fun bind(post: Post) {
            this.post = post

            with(binding) {
                authorName.text = post.author
                postMessage.text = post.content
                postDate.text = post.published
                likeButton.text = numToFormatString(post.likes)
                likeButton.isChecked = post.likedByMe
                shareButton.text = numToFormatString(post.shares)
                viewButton.text = numToFormatString(post.views)
                optionButton.setOnClickListener { popupMenu.show() }
                if (post.video != null) videoGroup.visibility =
                    View.VISIBLE else videoGroup.visibility = View.GONE
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListeners)
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