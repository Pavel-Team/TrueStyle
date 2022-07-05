package ru.dm.android.truestyle.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.dm.android.truestyle.api.Networking
import ru.dm.android.truestyle.repository.LoginRepository
import ru.dm.android.truestyle.repository.RegistrationRepository
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

}