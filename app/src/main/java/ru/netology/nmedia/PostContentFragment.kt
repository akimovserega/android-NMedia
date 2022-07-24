package ru.netology.nmedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.databinding.PostContentFragmentBinding

class PostContentFragment : Fragment() {


      private  val args by navArgs<PostContentFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostContentFragmentBinding.inflate(layoutInflater, container, false).also { binding ->

        binding.editWrite.setText(args.initialContent)
        binding.editWrite.requestFocus()
        binding.okButton.setOnClickListener {
            onOkButtonClicked(binding.editWrite.text?.toString())
        }
    }.root

    private fun onOkButtonClicked(postContent: String?) {

        if (!postContent.isNullOrBlank()) {
            val resultBundle = Bundle(1)
            resultBundle.putString(POST_CONTENT_EXTRA_KEY, postContent)
            setFragmentResult(REQUEST_KEY, resultBundle)
        }
        findNavController().popBackStack()
    }

     companion object {
        const val REQUEST_KEY = "requestKey"
        const val POST_CONTENT_EXTRA_KEY = "postContent"
         }


}