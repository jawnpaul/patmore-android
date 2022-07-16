package com.android.patmore.core.di

import com.android.patmore.features.foryou.data.repository.ForYouRepository
import com.android.patmore.features.foryou.domain.repository.IForYouRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BindsModule {

    @Binds
    abstract fun bindsForYouRepository(repository: ForYouRepository): IForYouRepository
}
