package com.patmore.android.features.foryou.data.repository

import com.patmore.android.core.api.PatmoreApiService
import com.patmore.android.core.api.TwitterApiService
import com.patmore.android.core.exception.Failure
import com.patmore.android.core.functional.Either
import com.patmore.android.core.utility.SharedPreferences
import com.patmore.android.features.foryou.data.remote.model.ForYouData
import com.patmore.android.features.foryou.data.remote.model.Res
import com.patmore.android.features.foryou.data.remote.model.SingleTweetResponse
import com.patmore.android.features.foryou.data.remote.model.TweetAuthor
import com.patmore.android.features.foryou.domain.model.ForYouTweet
import com.patmore.android.features.foryou.domain.model.TweetMedia
import com.patmore.android.features.foryou.domain.repository.IForYouRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class ForYouRepository @Inject constructor(
    private val patmoreApiService: PatmoreApiService,
    private val twitterApiService: TwitterApiService,
    private val sharedPreferences: SharedPreferences,
) : IForYouRepository {
    override suspend fun getCategoryTweets(category: String): Flow<Either<Failure, List<String>>> =
        flow {
            try {
                val res = patmoreApiService.getCategory(category)
                when (res.isSuccessful) {
                    true -> {
                        res.body()?.let { it ->
                            emit(Either.Right(it.response.map { aa -> aa.tweetId }))
                        } ?: emit(Either.Left(Failure.DataError))
                    }
                    false -> {
                        when {
                            res.code() == 404 -> {
                                Timber.d("404 error")
                                emit(Either.Left(Failure.UnAuthorizedError))
                            }
                            res.code() == 400 -> {
                                emit(Either.Left(Failure.BadRequest))
                            }
                            else -> {
                                emit(Either.Left(Failure.ServerError))
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Timber.e(e.stackTraceToString())
                emit(Either.Left(Failure.ServerError))
            }
        }

    override suspend fun getOriginalTweet(id: String): Flow<Either<Failure, List<ForYouTweet>>> =
        flow {
            try {
                val fields = "attachments,created_at"
                val expansions = "attachments.media_keys,author_id"
                val mediaFields = "type,media_key,preview_image_url,url"
                val userFields = "profile_image_url,name,username,id"
                val res = twitterApiService.getTweet(
                    id,
                    fields = fields,
                    expansions = expansions,
                    mediaFields = mediaFields,
                    userFields = userFields
                )

                when (res.isSuccessful) {
                    true -> {
                        res.body()?.let { it ->
                            val list = it.data.map { aa -> mapToDomain(it, aa) }
                            emit(Either.Right(list))
                        } ?: emit(Either.Left(Failure.DataError))
                    }
                    false -> {
                        when {
                            res.code() == 404 -> {
                                Timber.d("404 error")
                                emit(Either.Left(Failure.UnAuthorizedError))
                            }
                            res.code() == 400 -> {
                                emit(Either.Left(Failure.BadRequest))
                            }
                            else -> {
                                emit(Either.Left(Failure.ServerError))
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Timber.e(e.stackTraceToString())
                emit(Either.Left(Failure.ServerError))
            }
        }

    override suspend fun getForYouTweets(): Flow<Either<Failure, List<Pair<String, List<String>>>>> =
        flow {

            if (sharedPreferences.getUserCategories() != null) {
                try {

                    val res =
                        patmoreApiService.getUserSubscriptionTweets(
                            sharedPreferences.getUserCategories()!!,
                            page = 1
                        )
                    when (res.isSuccessful) {
                        true -> {
                            res.body()?.let { it ->
                                val ab = getPair(it.data)
                                emit(Either.Right(ab))
                            } ?: emit(Either.Left(Failure.DataError))
                        }
                        false -> {
                            when {
                                res.code() == 404 -> {
                                    Timber.d("404 error")
                                    emit(Either.Left(Failure.UnAuthorizedError))
                                }
                                res.code() == 400 -> {
                                    emit(Either.Left(Failure.BadRequest))
                                }
                                else -> {
                                    emit(Either.Left(Failure.ServerError))
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    Timber.e(e.stackTraceToString())
                }
            } else {
                Timber.e("Categories are null")
            }
        }

    private fun mapToDomain(data: SingleTweetResponse, response: Res): ForYouTweet {
        val keys = response.attachment?.mediaKeys
        val authors = data.includes?.tweetAuthors
        val ax = authors?.filter { it.id == response.authorId }?.get(0)
        val tweetAuthor = ax?.let {
            TweetAuthor(
                name = it.name,
                userName = "@" + it.userName,
                profileImage = it.profileImage.replace("_normal", ""),
                id = it.id
            )
        }
        val media = mutableListOf<TweetMedia>()
        data.includes?.media?.forEach {
            if (keys?.contains(it.mediaKey) == true) {
                media.add(it.toDomain())
            }
        }

        return ForYouTweet(
            id = response.id,
            text = response.text,
            tweetMedia = media,
            created = response.created,
            tweetAuthor = tweetAuthor
        )
    }

    private fun getPair(data: ForYouData): List<Pair<String, List<String>>> {
        val aa = mutableListOf<Pair<String, List<String>>>()

        data.anime?.let {
            val res = "anime" to it.map { xa -> xa.tweetId }
            aa.add(res)
        }

        data.technology?.let {
            val res = "technology" to it.map { xa -> xa.tweetId }
            aa.add(res)
        }

        data.sport?.let {
            val res = "sport" to it.map { xa -> xa.tweetId }
            aa.add(res)
        }

        data.music?.let {
            val res = "music" to it.map { xa -> xa.tweetId }
            aa.add(res)
        }

        data.travel?.let {
            val res = "travel" to it.map { xa -> xa.tweetId }
            aa.add(res)
        }

        data.business?.let {
            val res = "business" to it.map { xa -> xa.tweetId }
            aa.add(res)
        }

        return aa
    }
}
