package com.android.patmore.core.di

import com.android.patmore.core.imageloader.IImageLoader
import com.android.patmore.core.imageloader.ImageLoader
import com.android.patmore.features.authentication.data.repository.AuthenticationRepository
import com.android.patmore.features.authentication.domain.repository.IAuthenticationRepository
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

    @Binds
    abstract fun bindsImageLoader(imageLoader: ImageLoader): IImageLoader

    @Binds
    abstract fun bindsAuthenticationRepository(repository: AuthenticationRepository): IAuthenticationRepository
}
