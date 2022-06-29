package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.PostListBinding
import ru.netology.nmedia.util.hideKeyboard
import ru.netology.nmedia.viewModel.PostViewModel
import kotlin.math.floor
import kotlin.math.pow


class PostList : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = PostListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter(viewModel)

        binding.postRecycleView.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
        with(binding.editText) {
            binding.saveButton.setOnClickListener {
                val content = text.toString()
                viewModel.onSaveClicked(content)
                binding.groupEdit.visibility = View.GONE
            }
            binding.cancelButton.setOnClickListener {
                viewModel.onCancelClicked()
                text = null
                binding.groupEdit.visibility = View.GONE
            }

        }
        viewModel.currentPost.observe(this) { currentPost ->
            with(binding.editText) {
                val content = currentPost?.content
                setText(content)
                binding.postEditMessage.text = content

                if (content != null) {
                    requestFocus()
                    binding.groupEdit.visibility = View.VISIBLE

                } else {
                    clearFocus()
                    hideKeyboard()
                }

            }
        }
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

