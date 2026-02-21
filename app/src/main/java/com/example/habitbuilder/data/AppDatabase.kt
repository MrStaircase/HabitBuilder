package com.example.habitbuilder.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.habitbuilder.data.converter.CalendarConverter
import com.example.habitbuilder.data.dao.ActionDao
import com.example.habitbuilder.data.dao.CompletionDao
import com.example.habitbuilder.data.dao.RoutineDao
import com.example.habitbuilder.data.entity.ActionEntity
import com.example.habitbuilder.data.entity.CompletionEntity
import com.example.habitbuilder.data.entity.RoutineEntity

@Database(
    entities = [ActionEntity::class, RoutineEntity::class, CompletionEntity::class],
    version = 1
)
@TypeConverters(CalendarConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao
    abstract fun actionDao(): ActionDao
    abstract fun completeDao(): CompletionDao

    companion object DatabaseProvider {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "habitBuilderDatabase"
                )
                .build()
                .also { INSTANCE = it }
            }
        }
    }

}
