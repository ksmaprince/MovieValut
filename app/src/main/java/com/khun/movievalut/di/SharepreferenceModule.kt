package com.khun.movievalut.di

import android.content.Context
import com.khun.movievalut.data.local.PreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {
    @Provides
    fun providesSharedPreferenceManager(@ApplicationContext context: Context): PreferencesManager {
        return PreferencesManager(context = context)
    }
}