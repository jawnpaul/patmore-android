package com.android.patmore.features.custom.data.repository

import com.android.patmore.core.api.TwitterApiService
import com.android.patmore.core.exception.Failure
import com.android.patmore.core.functional.Either
import com.android.patmore.core.utility.SharedPreferences
import com.android.patmore.core.utility.analytics.MixPanelUtil
import com.android.patmore.features.custom.domain.model.TimelineTweet
import com.android.patmore.features.custom.domain.repository.ICustomRepository
import com.android.patmore.features.foryou.data.remote.model.Res
import com.android.patmore.features.foryou.data.remote.model.TweetAuthor
import com.android.patmore.features.foryou.data.remote.model.UserTimelineResponse
import com.android.patmore.features.foryou.domain.model.TweetMedia
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class CustomRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val twitterApiService: TwitterApiService,
) :
    ICustomRepository {

    @Inject
    lateinit var mixPanelUtil: MixPanelUtil

    override suspend fun getUserTimeline(): Flow<Either<Failure, List<TimelineTweet>>> = flow {
        try {
            // if userid is not null
            if (sharedPreferences.getTwitterUserId() != null) {

                // This will always be true
                if (System.currentTimeMillis() > sharedPreferences.getTokenExpiration()) {

                    val fields = "attachments,created_at"
                    val expansions = "attachments.media_keys,author_id"
                    val mediaFields = "type,media_key,preview_image_url,url"
                    val userFields = "profile_image_url,name,username,id"
                    val exclude = "retweets,replies"
                    val res =
                        twitterApiService.getUserHomeTimeline(
                            userId = sharedPreferences.getTwitterUserId()!!,
                            fields = fields,
                            expansions = expansions,
                            mediaFields = mediaFields,
                            userFields = userFields,
                            excludes = exclude
                        )
                    when (res.isSuccessful) {
                        true -> {
                            res.body()?.let {
                                val list = it.data.map { aa -> mapToDomain(it, aa) }
                                emit(Either.Right(list))
                            } ?: emit(Either.Left(Failure.DataError))
                        }
                        false -> {
                            when {
                                res.code() == 401 -> {
                                    Timber.d("401 error")
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
                } else {
                    // token has expired
                    // call refresh token
                }
            } else {
                emit(Either.Left(Failure.DataError))
            }
        } catch (e: Exception) {
            Timber.e(e.stackTraceToString())
            emit(Either.Left(Failure.ServerError))
        }
    }

    override suspend fun getCurrentUser(): Flow<Either<Failure, Unit>> = flow {

        try {
            val res = twitterApiService.getCurrentUser("profile_image_url")
            when (res.isSuccessful) {
                true -> {
                    res.body()?.let {
                        val user = it.user
                        sharedPreferences.saveTwitterUserId(user.id)
                        emit(Either.Right(Unit))
                        // mixpanel
                        mixPanelUtil.twitterLogin(user)
                    }
                }
                false -> {
                    when {
                        res.code() == 401 -> {
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

    private fun mapToDomain(data: UserTimelineResponse, response: Res): TimelineTweet {
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

        return TimelineTweet(
            id = response.id,
            text = response.text,
            tweetMedia = media,
            created = response.created,
            tweetAuthor = tweetAuthor
        )
    }
}
