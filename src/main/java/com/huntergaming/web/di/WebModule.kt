package com.huntergaming.web.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
class WebModule {

    @Provides
    internal fun provideOkHttpClient(): OkHttpClient = OkHttpClient()
}