package com.patmore.android.core.di

import android.content.Context
import androidx.room.Room
import com.patmore.android.core.database.PatmoreDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {
    companion object {
        @Singleton
        @Provides
        fun provideDataBase(@ApplicationContext context: Context): PatmoreDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                PatmoreDatabase::class.java,
                PatmoreDatabase.DB_NAME
            )
                .build()
        }

        @Singleton
        @Provides
        fun providesUserFollowerDao(db: PatmoreDatabase) = db.userFollowersDao()
    }
}
