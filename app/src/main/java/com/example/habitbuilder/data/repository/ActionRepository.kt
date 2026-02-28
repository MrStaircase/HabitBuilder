package com.example.habitbuilder.data.repository

import android.content.Context
import com.example.habitbuilder.data.AppDatabase
import com.example.habitbuilder.data.dao.ActionDao
import com.example.habitbuilder.data.entity.ActionEntity

object ActionRepository {
    private fun dao(context: Context) : ActionDao {
        return AppDatabase.getDatabase(context).actionDao()
    }

    suspend fun insert(context: Context, action: ActionEntity) : Long {
        return dao(context).insert(action)
    }

    suspend fun update(context: Context, action: ActionEntity){
        dao(context).update(action)
    }

    suspend fun updateDescription(context: Context, id: Int,  description: String){
        dao(context).updateDescription(id, description)
    }

    suspend fun updateDuration(context: Context, id: Int,  duration: Int){
        dao(context).updateDuration(id, duration)
    }

    suspend fun getAll(context: Context) : List<ActionEntity> {
        return dao(context).getAll()
    }

    suspend fun get(context: Context, id: Int) : ActionEntity?{
        return dao(context).get(id)
    }

    suspend fun getAll(context: Context, routineId: Int) : List<ActionEntity> {
        return dao(context).getAll(routineId)
    }

    suspend fun delete(context: Context, id: Int){
        dao(context).delete(id)
    }

    suspend fun delete(context: Context, action: ActionEntity){
        dao(context).delete(action)
    }

    suspend fun deleteAllByRoutine(context: Context, routineId: Int){
        dao(context).deleteAllByRoutine(routineId)
    }

    suspend fun deleteAll(context: Context){
        dao(context).deleteAll()
    }
}