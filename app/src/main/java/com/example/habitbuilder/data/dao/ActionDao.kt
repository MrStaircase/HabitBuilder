package com.example.habitbuilder.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.habitbuilder.data.entity.ActionEntity

@Dao
interface ActionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(action: ActionEntity) : Long

    @Update
    suspend fun update(action: ActionEntity)

    @Query("UPDATE actions SET description = :description WHERE id = :id")
    suspend fun updateDescription(id:Int, description: String)

    @Query("UPDATE actions SET durationMinutes = :duration WHERE id = :id")
    suspend fun updateDuration(id:Int, duration: Int)

    @Query("SELECT * FROM actions")
    suspend fun getAll(): List<ActionEntity>

    @Query("SELECT * FROM actions WHERE id = :id LIMIT 1")
    suspend fun get(id: Int): ActionEntity?

    @Query("SELECT * " +
            "FROM actions " +
            "WHERE " +
                "routineId = :routineId " +
            "ORDER BY id ASC")
    suspend fun getAll(routineId: Int): List<ActionEntity>

    @Query("DELETE FROM actions WHERE id = :id")
    suspend fun delete(id: Int)

    @Delete
    suspend fun delete(action: ActionEntity)

    @Query("DELETE FROM actions WHERE routineId = :routineId")
    suspend fun deleteAllByRoutine(routineId: Int)

    @Query("DELETE FROM actions")
    suspend fun deleteAll()
}
