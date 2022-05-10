package com.example.healthassistant.di

import com.example.healthassistant.data.substances.SubstanceParser
import com.example.healthassistant.data.substances.SubstanceParserInterface
import com.example.healthassistant.repository.SubstanceRepository
import com.example.healthassistant.repository.SubstanceRepositoryInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSubstanceParser(
        substanceParser: SubstanceParser
    ): SubstanceParserInterface

    @Binds
    @Singleton
    abstract fun bindSubstanceRepository(
        substanceRepository: SubstanceRepository
    ): SubstanceRepositoryInterface
}