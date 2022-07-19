package com.android.patmore.features.foryou.data.repository

import com.android.patmore.core.api.PatmoreApiService
import com.android.patmore.core.api.TwitterApiService
import com.android.patmore.core.exception.Failure
import com.android.patmore.core.functional.Either
import com.android.patmore.features.foryou.data.remote.model.Res
import com.android.patmore.features.foryou.data.remote.model.SingleTweetResponse
import com.android.patmore.features.foryou.domain.model.ForYouTweet
import com.android.patmore.features.foryou.domain.model.TweetMedia
import com.android.patmore.features.foryou.domain.repository.IForYouRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class ForYouRepository @Inject constructor(
    private val patmoreApiService: PatmoreApiService,
    private val twitterApiService: TwitterApiService,
) : IForYouRepository {
    override suspend fun getCategoryTweets(category: String): Flow<Either<Failure, List<String>>> =
        flow {
            try {
                val res = patmoreApiService.getCategory(category)
                when (res.isSuccessful) {
                    true -> {
                        res.body()?.let { it ->
                            emit(Either.Right(it.response.map { aa -> aa.tweetId }))
                        } ?: Either.Left(Failure.DataError)
                    }
                    false -> {
                        when {
                            res.code() == 404 -> {
                                Timber.d("404 error")
                                Either.Left(Failure.UnAuthorizedError)
                            }
                            res.code() == 400 -> {
                                Either.Left(Failure.BadRequest)
                            }
                            else -> {
                                Either.Left(Failure.ServerError)
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
                val fields = "attachments"
                val expansions = "attachments.media_keys"
                val mediaFields = "type,media_key,preview_image_url,url"
                val res = twitterApiService.getTweet(
                    id,
                    fields = fields,
                    expansions = expansions,
                    mediaFields = mediaFields
                )

                when (res.isSuccessful) {
                    true -> {
                        res.body()?.let { it ->
                            val list = it.data.map { aa -> mapToDomain(it, aa) }
                            emit(Either.Right(list))
                        } ?: Either.Left(Failure.DataError)
                    }
                    false -> {
                        when {
                            res.code() == 404 -> {
                                Timber.d("404 error")
                                Either.Left(Failure.UnAuthorizedError)
                            }
                            res.code() == 400 -> {
                                Either.Left(Failure.BadRequest)
                            }
                            else -> {
                                Either.Left(Failure.ServerError)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Timber.e(e.stackTraceToString())
                emit(Either.Left(Failure.ServerError))
            }
        }

    private fun mapToDomain(data: SingleTweetResponse, response: Res): ForYouTweet {
        val keys = response.attachment?.mediaKeys
        val media = mutableListOf<TweetMedia>()
        data.includes?.media?.forEach {
            if (keys?.contains(it.mediaKey) == true) {
                media.add(it.toDomain())
            }
        }

        return ForYouTweet(id = response.id, text = response.text, tweetMedia = media)
    }
}
