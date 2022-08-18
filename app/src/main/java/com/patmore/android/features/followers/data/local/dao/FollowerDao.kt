package com.patmore.android.features.followers.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.patmore.android.features.followers.data.local.model.FollowerEntity

@Dao
interface FollowerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(followerList: List<FollowerEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(followerEntity: FollowerEntity)
}
