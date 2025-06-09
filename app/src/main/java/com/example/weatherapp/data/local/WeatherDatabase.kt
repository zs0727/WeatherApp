// data/local/WeatherDatabase.kt
package com.example.weatherapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity::class], version = 2, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase? = null

        fun getDatabase(context: Context): WeatherDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WeatherDatabase::class.java,
                    "weather_database"
                ).fallbackToDestructiveMigration() // 添加这一行
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
