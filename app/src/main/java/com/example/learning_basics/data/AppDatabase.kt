package com.example.learning_basics.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserUrl::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase(){
    abstract fun urlDao() : UrlDao
}