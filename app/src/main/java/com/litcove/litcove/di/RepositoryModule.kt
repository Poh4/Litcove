package com.litcove.litcove.di

import com.litcove.litcove.data.api.ApiService
import com.litcove.litcove.data.repository.BookRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideBookRepository(
        apiService: ApiService
    ): BookRepository {
        return BookRepository(apiService)
    }
}