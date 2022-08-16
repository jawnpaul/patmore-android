package com.patmore.android.core.di

import com.patmore.android.core.imageloader.IImageLoader
import com.patmore.android.core.imageloader.ImageLoader
import com.patmore.android.features.authentication.data.repository.AuthenticationRepository
import com.patmore.android.features.authentication.domain.repository.IAuthenticationRepository
import com.patmore.android.features.custom.data.repository.CustomRepository
import com.patmore.android.features.custom.domain.repository.ICustomRepository
import com.patmore.android.features.followers.data.repository.FollowersRepository
import com.patmore.android.features.followers.domain.repository.IFollowersRepository
import com.patmore.android.features.foryou.data.repository.ForYouRepository
import com.patmore.android.features.foryou.domain.repository.IForYouRepository
import com.patmore.android.features.subscription.data.repository.SubscriptionRepository
import com.patmore.android.features.subscription.domain.repository.ISubscriptionRepository
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

    @Binds
    abstract fun bindsSubscriptionRepository(repository: SubscriptionRepository): ISubscriptionRepository

    @Binds
    abstract fun bindsCustomRepository(repository: CustomRepository): ICustomRepository

    @Binds
    abstract fun bindsFollowersRepository(repository: FollowersRepository): IFollowersRepository
}
