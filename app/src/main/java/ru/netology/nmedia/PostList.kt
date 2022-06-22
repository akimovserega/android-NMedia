package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.format.DateFormat
import android.text.format.DateUtils
import android.widget.DatePicker
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.annotation.DrawableRes
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.data.Post
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.PostBinding
import ru.netology.nmedia.databinding.PostListBinding

import ru.netology.nmedia.viewModel.PostViewModel
import java.lang.Math.pow
import kotlin.math.floor
import kotlin.math.pow
import kotlin.math.round

class PostList : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = PostListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter(
            onLikeClicked = { post ->
                viewModel.onLikeClicked(post)
            },
            onShareClicked = { post ->
                viewModel.onShareClicked(post)
            }

        )

        binding.postRecycleView.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
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

