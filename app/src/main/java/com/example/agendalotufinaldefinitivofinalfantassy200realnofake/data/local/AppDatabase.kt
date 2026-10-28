package com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.actividad.ActividadDao
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.actividad.ActividadEntity
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.alarm.AlarmDao
import com.example.agendalotufinaldefinitivofinalfantassy200realnofake.data.local.alarm.AlarmEntity

@Database(entities = [ActividadEntity::class, AlarmEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun actividadDao(): ActividadDao
    abstract fun alarmDao(): AlarmDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "agendalotu_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

