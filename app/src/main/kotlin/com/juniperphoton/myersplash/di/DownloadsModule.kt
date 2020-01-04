package com.juniperphoton.myersplash.di

import com.juniperphoton.myersplash.db.AppDatabase
import com.juniperphoton.myersplash.db.DownloadItemDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DownloadsModule {
    @Singleton
    @Provides
    fun providesDownloadsDao(): DownloadItemDao {
        return AppDatabase.instance.downloadItemDao()
    }
}