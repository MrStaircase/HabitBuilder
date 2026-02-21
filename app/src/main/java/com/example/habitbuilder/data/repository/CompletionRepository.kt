package com.example.habitbuilder.data.repository

import android.content.Context
import com.example.habitbuilder.data.AppDatabase
import com.example.habitbuilder.data.DailyCompletion
import com.example.habitbuilder.data.dao.CompletionDao
import com.example.habitbuilder.data.entity.CompletionEntity
import java.util.Calendar

object CompletionRepository {
    private fun dao(context: Context) : CompletionDao {
        return AppDatabase.getDatabase(context).completeDao()
    }

    suspend fun insert(context: Context, completion: CompletionEntity) {
        dao(context).insert(completion)
    }

    suspend fun getAll(context: Context) : List<CompletionEntity>{
        return dao(context).getAll()
    }

    suspend fun get(context: Context, routineId: Int) : List<CompletionEntity>{
        return dao(context).get(routineId)
    }

    suspend fun get(context: Context, routineId: Int, date: Calendar) : List<CompletionEntity>{
        return dao(context).get(routineId, date)
    }

    suspend fun getCountPerDay(context: Context, routineId: Int): List<DailyCompletion>{
        return dao(context).getCountPerDay(routineId)
    }

    suspend fun delete(context: Context, completion: CompletionEntity){
        dao(context).delete(completion)
    }

    suspend fun deleteAll(context: Context){
        dao(context).deleteAll()
    }
}