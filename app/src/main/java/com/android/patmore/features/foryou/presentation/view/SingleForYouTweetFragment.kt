package com.android.patmore.features.foryou.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.patmore.R
import com.android.patmore.core.imageloader.ImageLoader
import com.android.patmore.core.utility.capitalizeFirstLetter
import com.android.patmore.databinding.FragmentSingleForYouTweetBinding
import com.android.patmore.features.foryou.presentation.model.GifMediaPresentation
import com.android.patmore.features.foryou.presentation.model.ImageMediaPresentation
import com.android.patmore.features.foryou.presentation.model.TweetMediaPresentation
import com.android.patmore.features.foryou.presentation.model.VideoMediaPresentation
import com.android.patmore.features.foryou.presentation.viewmodel.ForYouViewModel
import com.google.android.material.imageview.ShapeableImageView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SingleForYouTweetFragment : Fragment() {
    private var _binding: FragmentSingleForYouTweetBinding? = null
    val binding get() = _binding!!

    private val forYouViewModel: ForYouViewModel by activityViewModels()

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSingleForYouTweetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mediaOne = view.findViewById(R.id.mediaOne) as ShapeableImageView
        val twoMediaContainer =
            view.findViewById(R.id.twoMediaContainer) as ConstraintLayout

        val threeMediaContainer =
            view.findViewById(R.id.threeMediaContainer) as ConstraintLayout
        val fourMediaContainer =
            view.findViewById(R.id.fourMediaContainer) as ConstraintLayout
        val videoContainer = view.findViewById(R.id.videoContainer) as FrameLayout
        val gifContainer = view.findViewById(R.id.gifContainer) as FrameLayout
        val gifImage = view.findViewById(R.id.gifImage) as ShapeableImageView
        val videoImage = view.findViewById(R.id.videoImage) as ShapeableImageView
        val twoImageMediaOne = view.findViewById(R.id.twoImageMediaOne) as ShapeableImageView
        val twoImageMediaTwo = view.findViewById(R.id.twoImageMediaTwo) as ShapeableImageView
        val threeImageMediaOne = view.findViewById(R.id.threeImageMediaOne) as ShapeableImageView
        val threeImageMediaTwo = view.findViewById(R.id.threeImageMediaTwo) as ShapeableImageView
        val threeImageMediaThree =
            view.findViewById(R.id.threeImageMediaThree) as ShapeableImageView
        val fourImageMediaOne = view.findViewById(R.id.fourImageMediaOne) as ShapeableImageView
        val fourImageMediaTwo = view.findViewById(R.id.fourImageMediaTwo) as ShapeableImageView
        val fourImageMediaThree =
            view.findViewById(R.id.fourImageMediaThree) as ShapeableImageView
        val fourImageMediaFour = view.findViewById(R.id.fourImageMediaFour) as ShapeableImageView

        fun renderPhotos(mediaContainer: ViewGroup, media: List<ImageMediaPresentation>) {
            mediaContainer.apply {
                mediaOne.visibility = if (media.size == 1) View.VISIBLE else View.GONE
                twoMediaContainer.visibility = if (media.size == 2) View.VISIBLE else View.GONE
                threeMediaContainer.visibility = if (media.size == 3) View.VISIBLE else View.GONE
                fourMediaContainer.visibility = if (media.size == 4) View.VISIBLE else View.GONE
                videoContainer.visibility = View.GONE
                gifContainer.visibility = View.GONE
                when (media.size) {
                    1 -> {
                        imageLoader.loadImage("${media[0].mediaUrl}:small", mediaOne)
                    }
                    2 -> {
                        imageLoader.loadImage("${media[0].mediaUrl}:small", twoImageMediaOne)

                        imageLoader.loadImage("${media[1].mediaUrl}:small", twoImageMediaTwo)
                    }
                    3 -> {
                        imageLoader.loadImage("${media[0].mediaUrl}:small", threeImageMediaOne)

                        imageLoader.loadImage("${media[1].mediaUrl}:small", threeImageMediaTwo)

                        imageLoader.loadImage("${media[2].mediaUrl}:small", threeImageMediaThree)
                    }
                    4 -> {
                        imageLoader.loadImage("${media[0].mediaUrl}:small", fourImageMediaOne)

                        imageLoader.loadImage("${media[1].mediaUrl}:small", fourImageMediaTwo)

                        imageLoader.loadImage("${media[2].mediaUrl}:small", fourImageMediaThree)

                        imageLoader.loadImage("${media[3].mediaUrl}:small", fourImageMediaFour)
                    }
                }
            }
        }

        fun renderMedia(
            mediaContainer: ViewGroup,
            media: List<TweetMediaPresentation>,
        ) {
            mediaContainer.visibility = View.VISIBLE

            when (media.size) {
                1 -> {
                    when (media[0]) {
                        is ImageMediaPresentation -> {
                            renderPhotos(mediaContainer, media as List<ImageMediaPresentation>)
                        }
                        is GifMediaPresentation -> {
                            val gif = media[0] as GifMediaPresentation
                            mediaContainer.run {
                                mediaOne.visibility = View.GONE
                                twoMediaContainer.visibility = View.GONE
                                threeMediaContainer.visibility = View.GONE
                                fourMediaContainer.visibility = View.GONE
                                videoContainer.visibility = View.GONE
                                gifContainer.visibility = View.VISIBLE
                                imageLoader.loadImage("${gif.mediaPreviewUrl}:small", gifImage)

                                /*gifContainer.setOnClickListener {
                                    onMediaClickListener.onVideoOrGifClick(media[0].url)
                                }*/
                            }
                        }
                        is VideoMediaPresentation -> {
                            val video = media[0] as VideoMediaPresentation
                            mediaContainer.run {
                                mediaOne.visibility = View.GONE
                                twoMediaContainer.visibility = View.GONE
                                threeMediaContainer.visibility = View.GONE
                                fourMediaContainer.visibility = View.GONE
                                videoContainer.visibility = View.VISIBLE
                                gifContainer.visibility = View.GONE
                                imageLoader.loadImage("${video.mediaPreviewUrl}:small", videoImage)

                                /*videoContainer.setOnClickListener {
                                    onMediaClickListener.onVideoOrGifClick(media[0].url)
                                }*/
                            }
                        }
                    }
                }
                else -> {
                    renderPhotos(mediaContainer, media as List<ImageMediaPresentation>)
                }
            }
        }

        /*lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                forYouViewModel.singleTweet.collect { state ->
                    state.data?.let {
                        it.mediaList?.let { it1 -> renderMedia(binding.singleTweetMediaContainer, it1) }
                        binding.toolbarTitle.text = it.category?.lowercase()?.capitalizeFirstLetter()
                        binding.singleTweetText.text = it.text
                    }
                }
            }
        }*/

        forYouViewModel.selectedTweet.observe(viewLifecycleOwner) { state ->
            state.data?.let {

                it.mediaList?.let { it1 -> renderMedia(binding.singleTweetMediaContainer, it1) }
                binding.toolbarTitle.text = it.category?.lowercase()?.capitalizeFirstLetter()
                binding.singleTweetText.text = it.text

                it.tweetAuthor?.let { tweetAuthor ->

                    binding.userHandleTextView.text = tweetAuthor.userName
                    binding.userNameTextView.text = tweetAuthor.name

                    imageLoader.loadCircleImage(tweetAuthor.profileImage, binding.profileImageView)
                }
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}
