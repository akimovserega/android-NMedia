package ru.netology.nmedia

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.PostFragmentBinding
import ru.netology.nmedia.viewModel.PostViewModel


class PostFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    private val args by navArgs<PostFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostFragmentBinding.inflate(layoutInflater, container, false).also { binding ->


        val viewHolder = PostsAdapter.ViewHolder(binding.postLayout, viewModel)


        viewModel.shareEvent.observe(viewLifecycleOwner) { post ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, post.content)
            }
            val shareIntent = Intent.createChooser(intent, getString(R.string.toShare))
            startActivity(shareIntent)
        }

        viewModel.playEvent.observe(viewLifecycleOwner) { post ->
            val playIntent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
            startActivity(playIntent)
        }

        viewModel.addEvent.observe(viewLifecycleOwner) { initialContent ->
            val direction = PostFragmentDirections.toPostContentFragment(initialContent)
            findNavController().navigate(direction)
        }


        setFragmentResultListener(
            requestKey = PostContentFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
            val newPostContent = bundle.getString(PostContentFragment.POST_CONTENT_EXTRA_KEY)
                ?: return@setFragmentResultListener
            viewModel.onCreateNewPost(newPostContent)

        }

        viewModel.data.observe(viewLifecycleOwner) { posts ->

            val post = posts.find { it.id == args.postId }

            if (post != null) {
                viewHolder.bind(post)
            } else {
                findNavController().popBackStack()
            }
        }


    }.root

    companion object {
    }
}