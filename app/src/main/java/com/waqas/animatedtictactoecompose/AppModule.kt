package com.waqas.animatedtictactoecompose

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun providePair(): Pair<String,String>{
        return Pair("WA","AS")
    }
}