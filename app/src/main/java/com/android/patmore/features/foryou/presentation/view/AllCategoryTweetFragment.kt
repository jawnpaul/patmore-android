package com.android.patmore.features.foryou.presentation.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.android.patmore.R
import com.android.patmore.core.imageloader.ImageLoader
import com.android.patmore.core.utility.analytics.MixPanelUtil
import com.android.patmore.databinding.FragmentAllCategoryTweetBinding
import com.android.patmore.features.foryou.presentation.adapter.CategoryPagerAdapter
import com.android.patmore.features.foryou.presentation.model.ForYouTweetPresentation
import com.android.patmore.features.foryou.presentation.viewmodel.ForYouViewModel
import com.android.patmore.features.media.GifMediaPresentation
import com.android.patmore.features.media.ImageMediaPresentation
import com.android.patmore.features.media.TweetMediaPresentation
import com.android.patmore.features.media.VideoMediaPresentation
import com.google.android.material.imageview.ShapeableImageView
import com.teresaholfeld.stories.StoriesProgressView
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class AllCategoryTweetFragment : Fragment(), StoriesProgressView.StoriesListener {
    private var _binding: FragmentAllCategoryTweetBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var mixPanelUtil: MixPanelUtil

    @Inject
    lateinit var imageLoader: ImageLoader

    private lateinit var categoryPagerAdapter: CategoryPagerAdapter
    private lateinit var viewPager: ViewPager2

    private val forYouViewModel: ForYouViewModel by activityViewModels()

    private var storiesProgressView: StoriesProgressView? = null
    private var image: ImageView? = null

    private var counter = 0

    private var categoryTweets = listOf<ForYouTweetPresentation>()

    private var pressTime = 0L
    private var limit = 500L

    @SuppressLint("ClickableViewAccessibility")
    private val onTouchListener = View.OnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                pressTime = System.currentTimeMillis()
                storiesProgressView?.pause()
                return@OnTouchListener false
            }
            MotionEvent.ACTION_UP -> {
                val now = System.currentTimeMillis()
                storiesProgressView?.resume()
                return@OnTouchListener limit < now - pressTime
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentAllCategoryTweetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        forYouViewModel.showBottomNavBar(false)

        /*viewPager = view.findViewById(R.id.pager)

        val toolbarTitle: TextView = view.findViewById(R.id.all_toolbar_title)
        val toolbar: MaterialToolbar = view.findViewById(R.id.all_toolbar)
        val tabLayout: TabLayout = view.findViewById(R.id.tab_layout)

        forYouViewModel.selectedCategory.observe(viewLifecycleOwner) {
            toolbarTitle.text = it?.lowercase()?.capitalizeFirstLetter()
        }

        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        forYouViewModel.selectedCategoryFragment.observe(viewLifecycleOwner) {
            categoryPagerAdapter = CategoryPagerAdapter(this, it)

            viewPager.adapter = categoryPagerAdapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            }.attach()
        }*/

        storiesProgressView = view.findViewById(R.id.stories)
        // image = view.findViewById(R.id.background_image)

        forYouViewModel.currentPosition.observe(viewLifecycleOwner) {

            // storiesProgressView?.startStories(it)
        }

        // bind reverse view
        val reverse = view.findViewById<View>(R.id.reverse)
        reverse.setOnClickListener {
            storiesProgressView?.reverse()
            Timber.d("Reverse")
        }
        reverse.setOnTouchListener(onTouchListener)

        // bind skip view
        val skip = view.findViewById<View>(R.id.skip)
        skip.setOnClickListener {
            storiesProgressView?.skip()
            Timber.d("Skip")
        }
        skip.setOnTouchListener(onTouchListener)

        forYouViewModel.selectedView.observe(viewLifecycleOwner) {
            it.data?.let { tweets ->
                categoryTweets = tweets
                // counter = tweets.size
                // storiesProgressView?.setStoriesCount(tweets.size)
                Timber.d("Here")

                storiesProgressView?.setStoriesCount(tweets.size) // <- set stories
                storiesProgressView?.setStoryDuration(3000L) // <- set a story duration
                storiesProgressView?.setStoriesListener(this) // <- set listener
                counter = 0
                storiesProgressView?.startStories(counter) // <- start progress

                val mediaList = categoryTweets[counter].mediaList
                mediaList?.let { aa ->
                    when (val first = aa[0]) {
                        is ImageMediaPresentation -> {

                            /*val pal =
                                imageLoader.getPalette("${first.mediaUrl}:small", palette = { xa ->
                                    Timber.e(xa?.swatches?.size.toString())
                                    xa?.let { bb ->
                                        binding.backgroundImage
                                        bb.mutedSwatch?.let { zz -> binding.backgroundImage.setBackgroundColor(zz.bodyTextColor) }
                                    }
                                })*/
                        }
                        is GifMediaPresentation -> {
                            // imageLoader.getPalette("${first.mediaPreviewUrl}:small")
                        }
                        is VideoMediaPresentation -> {
                            // imageLoader.getPalette("${first.mediaPreviewUrl}:small")
                        }
                        else -> {
                        }
                    }
                }
            }
        }

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

        forYouViewModel.currentTweet.observe(viewLifecycleOwner) { tweetPresentation ->

            tweetPresentation.let {
                binding.singleTweetText.text = tweetPresentation.text

                it.mediaList?.let { it1 -> renderMedia(binding.singleTweetMediaContainer, it1) }

                it.tweetAuthor?.let { tweetAuthor ->

                    binding.userHandleTextView.text = tweetAuthor.userName
                    binding.userNameTextView.text = tweetAuthor.name

                    imageLoader.loadCircleImage(tweetAuthor.profileImage, binding.profileImageView)
                }
            }
        }
    }

    override fun onComplete() {
    }

    override fun onNext() {
        val inner = ++counter

        forYouViewModel.setCurrentTweet(categoryTweets[inner])

        val mediaList = categoryTweets[inner].mediaList
        mediaList?.let { aa ->
            when (val first = aa[0]) {
                is ImageMediaPresentation -> {
                    /*val pal = imageLoader.getPalette("${first.mediaUrl}:small", palette = { xa ->
                        Timber.e(xa?.swatches?.size.toString())
                        xa?.let { bb ->
                            bb.mutedSwatch?.let { zz -> binding.backgroundImage.setBackgroundColor(zz.bodyTextColor) }
                        }
                    })*/
                }
                is GifMediaPresentation -> {
                    // imageLoader.getPalette("${first.mediaPreviewUrl}:small")
                }
                is VideoMediaPresentation -> {
                    // imageLoader.getPalette("${first.mediaPreviewUrl}:small")
                }
                else -> {
                }
            }
        }
    }

    override fun onPrev() {
        if (counter - 1 < 0) return
        val inner = --counter
        forYouViewModel.setCurrentTweet(categoryTweets[inner])

        val mediaList = categoryTweets[inner].mediaList
        mediaList?.let { aa ->
            when (val first = aa[0]) {
                is ImageMediaPresentation -> {
                    /*val pal = imageLoader.getPalette("${first.mediaUrl}:small", palette = { xa ->
                        Timber.e(xa?.swatches?.size.toString())
                        xa?.let { bb ->
                            bb.mutedSwatch?.let { zz -> binding.backgroundImage.setBackgroundColor(zz.bodyTextColor) }
                        }
                    })*/
                }
                is GifMediaPresentation -> {
                    // imageLoader.getPalette("${first.mediaPreviewUrl}:small")
                }
                is VideoMediaPresentation -> {
                    // imageLoader.getPalette("${first.mediaPreviewUrl}:small")
                }
                else -> {
                }
            }
        }
    }

    override fun onDestroy() {
        storiesProgressView?.destroy()
        super.onDestroy()
    }
}
