package com.patmore.android.features.followers.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "follower_table")
data class FollowerEntity(
    @PrimaryKey val id: String,
    val name: String,
    val userName: String,
    val profileImageUrl: String,
)
