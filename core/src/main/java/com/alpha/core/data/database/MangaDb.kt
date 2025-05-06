package com.alpha.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserEntity::class, MangaEntity::class], version = 2, exportSchema = false)
abstract class MangaDb : RoomDatabase() {
    abstract fun userDao(): UserDao
}
