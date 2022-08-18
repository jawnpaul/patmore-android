package com.patmore.android.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.patmore.android.features.followers.data.local.dao.FollowerDao
import com.patmore.android.features.followers.data.local.model.FollowerEntity

@Database(entities = [FollowerEntity::class], version = 1, exportSchema = true)
abstract class PatmoreDatabase : RoomDatabase() {

    companion object {
        const val DB_NAME = "patmore_database"
    }

    abstract fun userFollowersDao(): FollowerDao
}
