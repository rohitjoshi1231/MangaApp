package com.alpha.core.data.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.alpha.core.data.database.MangaDb
import com.alpha.core.data.database.UserDao
import com.alpha.core.data.session.UserSessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {


    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL(
                """
            CREATE TABLE IF NOT EXISTS `manga` (
                `id` TEXT NOT NULL,
                `title` TEXT NOT NULL,
                `subTitle` TEXT NOT NULL,
                `thumb` TEXT NOT NULL,
                `summary` TEXT NOT NULL,
                `genres` TEXT NOT NULL,
                PRIMARY KEY(`id`)
            )
        """
            )
        }
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MangaDb {
        return Room.databaseBuilder(context, MangaDb::class.java, "app_database")
            .fallbackToDestructiveMigration().build()
    }


    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideUserSessionManager(@ApplicationContext context: Context): UserSessionManager {
        return UserSessionManager(context)
    }

    @Provides
    fun provideUserDao(db: MangaDb): UserDao = db.userDao()
}
