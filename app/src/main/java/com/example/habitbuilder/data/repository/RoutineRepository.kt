package com.example.habitbuilder.data.repository

import android.content.Context
import androidx.room.Query
import com.example.habitbuilder.data.AppDatabase
import com.example.habitbuilder.data.dao.RoutineDao
import com.example.habitbuilder.data.entity.RoutineEntity
import java.util.Calendar

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

    suspend fun updateName(context: Context, id:Int, name: String){
        dao(context).updateName(id, name)
    }

    suspend fun updateTriggerTime(context: Context, id:Int, triggerTime: Calendar){
        dao(context).updateTriggerTime(id, triggerTime)
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