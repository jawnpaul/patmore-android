package com.patmore.android.features.followers.domain.repository

import com.patmore.android.core.exception.Failure
import com.patmore.android.core.functional.Either
import kotlinx.coroutines.flow.Flow

interface IFollowersRepository {

    suspend fun getUserFollowers(): Flow<Either<Failure, Unit>>
}
