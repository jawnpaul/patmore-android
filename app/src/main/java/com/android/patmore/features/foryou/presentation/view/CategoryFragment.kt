package com.android.patmore.features.foryou.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.android.patmore.R
import com.android.patmore.core.imageloader.ImageLoader
import com.android.patmore.core.utility.analytics.MixPanelUtil
import com.android.patmore.features.foryou.presentation.model.ForYouTweetPresentation
import com.android.patmore.features.foryou.presentation.model.ImageMediaPresentation
import com.android.patmore.features.foryou.presentation.model.VideoMediaPresentation
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val ARG_PARAM1 = "param1"

@AndroidEntryPoint
class CategoryFragment : Fragment() {
    private var param1: ForYouTweetPresentation? = null

    private val TAG = CategoryFragment::class.simpleName

    @Inject
    lateinit var mixPanelUtil: MixPanelUtil

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getParcelable(ARG_PARAM1)
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
        titleText.text = param1?.text ?: ""
        val relativeTime: TextView = view.findViewById(R.id.category_tweet_text_time)
        relativeTime.text = param1?.created ?: ""

        val imageView: ShapeableImageView = view.findViewById(R.id.category_image_view)

        param1?.mediaList?.let {
            if (it.isNotEmpty()) {
                val first = it[0]
                when (first) {
                    is ImageMediaPresentation -> {
                        val mediaUrl = first.mediaUrl
                        if (mediaUrl != null) {
                            imageLoader.loadImage(mediaUrl, imageView)
                        }
                    }
                    is VideoMediaPresentation -> {
                    }
                }
            }
        }

        mixPanelUtil.logScreen(TAG)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: ForYouTweetPresentation) =
            CategoryFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_PARAM1, param1)
                }
            }
    }
}
