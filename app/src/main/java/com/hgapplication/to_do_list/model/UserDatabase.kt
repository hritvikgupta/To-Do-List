package com.hgapplication.to_do_list.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [User::class,Tasks::class, Items::class], version = 27)
@TypeConverters(taskListTypeConverters::class)

abstract class UserDatabase: RoomDatabase() {
    abstract fun userDao(): userDao
    abstract fun taskDao():taskDao
    companion object {
        @Volatile private var instance: UserDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            UserDatabase::class.java,
            "userdatabase"
        ).fallbackToDestructiveMigration().build()
    }
}
