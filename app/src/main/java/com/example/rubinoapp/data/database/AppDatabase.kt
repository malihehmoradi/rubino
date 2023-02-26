package com.example.rubinoapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Post::class], version = 1)
@TypeConverters(value = [TypeConvertor::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao
}