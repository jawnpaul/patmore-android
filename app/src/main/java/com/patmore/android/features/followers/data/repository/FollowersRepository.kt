package com.patmore.android.features.followers.data.repository

import com.patmore.android.core.api.TwitterApiService
import com.patmore.android.core.exception.Failure
import com.patmore.android.core.functional.Either
import com.patmore.android.core.utility.SharedPreferences
import com.patmore.android.features.followers.data.local.dao.FollowerDao
import com.patmore.android.features.followers.domain.repository.IFollowersRepository
import com.patmore.android.features.foryou.data.remote.model.TweetAuthor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject

class FollowersRepository @Inject constructor(
    private val twitterApiService: TwitterApiService,
    private val followerDao: FollowerDao,
    private val sharedPreferences: SharedPreferences,
) :
    IFollowersRepository {
    override suspend fun getUserFollowers(): Flow<Either<Failure, Unit>> = flow {
        try {
            var res = makeApiCall(null)
            while (res.first) {
                if (res.second.isNotEmpty()) {
                    delay(1000L)
                    res = makeApiCall(res.second)
                }
            }

            emit(Either.Right(Unit))
        } catch (e: Exception) {
            Timber.e(e.stackTraceToString())
            emit(Either.Left(Failure.ServerError))
        }
    }

    private suspend fun saveFollowerList(users: List<TweetAuthor>) {
        val entities = users.map { it.toFollowerEntity() }
        followerDao.insertAll(entities)
    }

    private suspend fun makeApiCall(nextToken: String?): Pair<Boolean, String> {
        var result = Pair(false, "")
        try {

            if (sharedPreferences.getTwitterUserId() != null) {
                val res =
                    twitterApiService.getUserFollowing(
                        userId = sharedPreferences.getTwitterUserId()!!,
                        userFields = "profile_image_url",
                        paginationToken = nextToken,
                        maxResults = 1000
                    )
                when (res.isSuccessful) {
                    true -> {
                        res.body()?.let { data ->
                            // TODO:Uncomment line below

                            // saveFollowerList(data.data)
                            data.meta.nextToken?.let { t ->
                                result = Pair(true, t)
                            }
                        }
                    }
                    false -> {
                        result = Pair(false, "")
                    }
                }
            }
        } catch (e: Exception) {
            Timber.e(e.stackTraceToString())
        }
        return result
    }
}
