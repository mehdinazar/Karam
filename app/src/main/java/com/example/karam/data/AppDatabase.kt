package com.example.karam.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.karam.data.dao.CategoryDao
import com.example.karam.data.dao.TaskDao
import com.example.karam.data.model.Category
import com.example.karam.data.model.Task
import com.example.karam.util.DateConverter

@Database(entities = [Task::class, Category::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "karam_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
} 