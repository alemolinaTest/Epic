package com.amolina.epic.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DateEntity::class, DateImageEntity::class],
          version = 1) abstract class EpicDataBase(): RoomDatabase() {
  abstract fun epicDao(): EpicDao
}