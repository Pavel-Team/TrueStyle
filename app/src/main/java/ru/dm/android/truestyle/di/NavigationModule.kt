package ru.dm.android.truestyle.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.ui.navigation.Navigation
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NavigationModule {

    @Provides
    @Singleton
    fun providesNavigation(): Navigation {
        return Navigation()
    }

}