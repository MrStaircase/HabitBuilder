package com.example.habitbuilder.data.repository

import android.content.Context
import com.example.habitbuilder.data.AppDatabase
import com.example.habitbuilder.data.dao.RoutineDao
import com.example.habitbuilder.data.entity.RoutineEntity

object RoutineRepository{
    private fun dao(context: Context) : RoutineDao {
        return AppDatabase.getDatabase(context).routineDao()
    }

    suspend fun insert(context: Context, routine: RoutineEntity) : Long {
        return dao(context).insert(routine)
    }

    suspend fun update(context: Context, routine: RoutineEntity){
        dao(context).update(routine)
    }

    suspend fun getAll(context: Context) : List<RoutineEntity> {
        return dao(context).getAll()
    }

    suspend fun get(context: Context, id: Int) : RoutineEntity?{
        return dao(context).get(id)
    }

    suspend fun delete(context: Context, id: Int){
        dao(context).delete(id)
    }

    suspend fun delete(context: Context, routine: RoutineEntity){
        dao(context).delete(routine)
    }

    suspend fun deleteAll(context: Context){
        dao(context).deleteAll()
    }
}