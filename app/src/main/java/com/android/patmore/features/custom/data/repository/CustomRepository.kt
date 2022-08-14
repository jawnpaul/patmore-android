package com.android.patmore.features.custom.data.repository

import com.android.patmore.core.api.CustomTwitterApiService
import com.android.patmore.core.api.TwitterApiService
import com.android.patmore.core.exception.Failure
import com.android.patmore.core.functional.Either
import com.android.patmore.core.utility.SharedPreferences
import com.android.patmore.core.utility.analytics.MixPanelUtil
import com.android.patmore.features.custom.domain.repository.ICustomRepository
import com.tycz.tweedle.lib.authentication.oauth.OAuth1
import com.tycz.tweedle.lib.tweets.lookup.TweetsLookup
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class CustomRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val twitterApiService: TwitterApiService,
    private val customTwitterApiService: CustomTwitterApiService,
) :
    ICustomRepository {

    @Inject
    lateinit var mixPanelUtil: MixPanelUtil

    override suspend fun getUserTimeline(): Flow<Either<Failure, Unit>> = flow {
        try {
            // if userid is not null
            if (sharedPreferences.getTwitterUserId() != null) {
                // if token hasn't expired
                Timber.d("User id is not null")
                Timber.d(sharedPreferences.getTokenExpiration().toString())
                if (System.currentTimeMillis() > sharedPreferences.getTokenExpiration()) {

                    val fields = "attachments,created_at"
                    val expansions = "attachments.media_keys,author_id"
                    val mediaFields = "type,media_key,preview_image_url,url"
                    val userFields = "profile_image_url,name,username,id"
                    val res =
                        twitterApiService.getUserHomeTimeline(
                            userId = sharedPreferences.getTwitterUserId()!!,
                            fields = fields,
                            expansions = expansions,
                            mediaFields = mediaFields,
                            userFields = userFields
                        )
                    when (res.isSuccessful) {
                        true -> {
                            res.body()?.let {
                                Timber.d(it.data[0].text)
                                emit(Either.Right(Unit))
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
        if (sharedPreferences.getTwitterUserId() == null) {
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
        } else {
            emit(Either.Right(Unit))
        }
    }

    override suspend fun getTimeline(): Flow<Either<Failure, Unit>> = flow {
        val oauth1 = OAuth1("", "", "", "")

        val s = TweetsLookup(oauth1)
    }
}
