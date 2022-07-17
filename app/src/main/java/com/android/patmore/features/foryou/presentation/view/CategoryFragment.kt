package com.android.patmore.features.foryou.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.patmore.R
import com.android.patmore.core.imageloader.ImageLoader
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class CategoryFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val titleText: TextView = view.findViewById(R.id.category_tweet_text)
        titleText.text = param1
        val relativeTime: TextView = view.findViewById(R.id.category_tweet_text_time)
        relativeTime.text = param2
        val imageView: ShapeableImageView = view.findViewById(R.id.category_image_view)
        imageLoader.loadImage("https://pbs.twimg.com/media/FX4-0fzWYAIV0Td.jpg", imageView)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CategoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
