package com.android.patmore.features.foryou.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.patmore.core.exception.Failure
import com.android.patmore.core.functional.onFailure
import com.android.patmore.core.functional.onSuccess
import com.android.patmore.features.foryou.domain.usecases.GetCategoryTweetUseCase
import com.android.patmore.features.foryou.domain.usecases.GetForYouTweetsUseCase
import com.android.patmore.features.foryou.domain.usecases.GetSingleOriginalTweetUseCase
import com.android.patmore.features.foryou.presentation.model.CategoryTweetItem
import com.android.patmore.features.foryou.presentation.model.ForYouTweetPresentation
import com.android.patmore.features.foryou.presentation.model.SingleCategoryTweetItem
import com.xwray.groupie.Section
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ForYouViewModel @Inject constructor(
    private val getCategoryTweetUseCase: GetCategoryTweetUseCase,
    private val getSingleOriginalTweetUseCase: GetSingleOriginalTweetUseCase,
    private val getForYouTweetsUseCase: GetForYouTweetsUseCase,
) :
    ViewModel() {

    private val job = Job()

    private val _technologyTweets = MutableLiveData<List<ForYouTweetPresentation>>()
    val technologyTweets get() = _technologyTweets

    // TODO:Change Livedata to stateflow
    private val _sectionList = MutableLiveData<List<Section>>()
    val sectionList: LiveData<List<Section>> get() = _sectionList

    private var techList = listOf<String>()
    private var animeList = listOf<String>()
    private var musicList = listOf<String>()
    private var travelList = listOf<String>()
    private var sportList = listOf<String>()
    private var businessList = listOf<String>()
    private var availableSections = listOf<String>()

    fun getTechnologyTweets() {
        getCategoryTweetUseCase(job, "technology") {
            it.onFailure { failure -> Timber.e(failure.toString()) }
            it.onSuccess { result ->
                // getOriginalTechnologyTweet(result)
            }
        }
    }

    private fun getOriginalTweets(ids: List<String>) {
        val id = getIds(ids)
        getSingleOriginalTweetUseCase(job, id) {
            it.onFailure { failure -> Timber.e(failure.toString()) }
            it.onSuccess { result ->
                Timber.d("Tweet Gotten")
                // need to map the presentation to category
                // if id in tech list, add
                val tech = result.filter { aa -> techList.contains(aa.id) }
                val anime = result.filter { aa -> animeList.contains(aa.id) }
                val music = result.filter { aa -> musicList.contains(aa.id) }
                val travel = result.filter { aa -> travelList.contains(aa.id) }
                val sport = result.filter { aa -> sportList.contains(aa.id) }
                val business = result.filter { aa -> businessList.contains(aa.id) }
                // section.addAll(techcategory)
                // _technologyTweets.value = result.map { aa -> aa.toPresentation() }

                if (result.isNotEmpty()) {

                    val aab = arrayListOf<Section>()

                    val items = result.map { ax -> SingleCategoryTweetItem(ax.toPresentation()) }
                    val ti = tech.map { ax -> SingleCategoryTweetItem(ax.toPresentation()) }
                    val bi = business.map { ax -> SingleCategoryTweetItem(ax.toPresentation()) }

                    // need a list of sections
                    // use the id of the item to get its section

                    val aa = Section()
                    aa.add(CategoryTweetItem("Technology", ti))
                    aa.add(CategoryTweetItem("Business", bi))
                    aab.add(aa)

                    /*val xx = result.forEach {
                        val aa = Section()
                        aa.add(CategoryTweetItem("tech", items))
                        aab.add(aa)
                    }*/
                    _sectionList.value = aab
                } else {
                    _sectionList.value = emptyList()
                }
            }
        }
    }

    private fun getIds(ids: List<String>): String {
        var res = ""
        ids.forEach {
            res += ",$it"
        }
        res = res.removePrefix(",")
        return res
    }

    fun getForYouTweets() {
        getForYouTweetsUseCase(job, GetForYouTweetsUseCase.None()) {
            it.fold(
                ::handleForYouFailure,
                ::handleForYouSuccess
            )
        }
    }

    private fun handleForYouFailure(failure: Failure) {
        // TODO:Update UI ask user to refresh
    }

    private fun handleForYouSuccess(response: List<Pair<String, List<String>>>) {
        val ids = response.flatMap { pair -> pair.second.map { it } }
        // make call to twitter api
        getOriginalTweets(ids)
        availableSections = response.map { pair -> pair.first }

        techList = response.filter { pair -> pair.first == "technology" }.flatMap { it.second }
        animeList = response.filter { pair -> pair.first == "anime" }.flatMap { it.second }
        sportList = response.filter { pair -> pair.first == "sport" }.flatMap { it.second }
        travelList = response.filter { pair -> pair.first == "travel" }.flatMap { it.second }
        businessList = response.filter { pair -> pair.first == "business" }.flatMap { it.second }
        musicList = response.filter { pair -> pair.first == "music" }.flatMap { it.second }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}
