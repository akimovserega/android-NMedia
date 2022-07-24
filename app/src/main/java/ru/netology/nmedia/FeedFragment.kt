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
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FeedFragmentBinding
import ru.netology.nmedia.viewModel.PostViewModel
import kotlin.math.floor
import kotlin.math.pow


class FeedFragment : Fragment() {


    private val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FeedFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

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

        setFragmentResultListener(
            requestKey = PostContentFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
            val newPostContent = bundle.getString(PostContentFragment.POST_CONTENT_EXTRA_KEY)
                ?: return@setFragmentResultListener
            viewModel.onCreateNewPost(newPostContent)

        }


        viewModel.addEvent.observe(viewLifecycleOwner) { initialContent ->
            val direction = FeedFragmentDirections.toPostContentFragment(initialContent)
            findNavController().navigate(direction)
        }

        viewModel.tapEvent.observe(viewLifecycleOwner) { postId ->
            val direction = FeedFragmentDirections.toPostFragment(postId)
            findNavController().navigate(direction)
        }

        val adapter = PostsAdapter(viewModel)

        binding.postRecycleView.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }
        binding.editButton.setOnClickListener {
            viewModel.onAddClicked()
        }

    }.root

    companion object {
    }
}

fun numToFormatString(count: Int): String {
    var resultString = count.toString()

    var suffix = ""
    var degree = 0

    if (count in 1_000..999999) {
        suffix = "K"
        degree = 1
    } else if (count in 1_000_000..999999999) {
        suffix = "M"
        degree = 2
    } else if (count in 1_000_000_000..Int.MAX_VALUE) {
        suffix = "B"
        degree = 3
    }


    if (degree > 0) {
        resultString = floor(count / 1000.0.pow(degree)).toInt().toString()
        if (count < 10 * 1000.0.pow(degree)) {
            val decimal =
                floor((count % 1000.0.pow(degree)) / (100 * 1000.0.pow(degree - 1))).toInt()
            if (decimal > 0) {
                resultString += ".$decimal"
            }
        }
    }
    return resultString + suffix
}

