package ru.dm.android.truestyle.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.repository.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesLoginRepository(networking: Networking): LoginRepository {
        return LoginRepository(networking)
    }

    @Provides
    @Singleton
    fun providesRegistrationRepository(networking: Networking): RegistrationRepository {
        return RegistrationRepository(networking)
    }

    @Provides
    @Singleton
    fun providesRecommendationRepository(networking: Networking): RecommendationRepository {
        return RecommendationRepository(networking)
    }

    @Provides
    @Singleton
    fun providesArticlesRepository(networking: Networking): ArticlesRepository {
        return ArticlesRepository(networking)
    }

    @Provides
    @Singleton
    fun providesProfileRepository(networking: Networking): ProfileRepository {
        return ProfileRepository(networking)
    }

    @Provides
    @Singleton
    fun providesWardrobeRepository(networking: Networking): WardrobeRepository {
        return WardrobeRepository(networking)
    }

    @Provides
    @Singleton
    fun providesStuffRepository(networking: Networking): StuffRepository {
        return StuffRepository(networking)
    }

}