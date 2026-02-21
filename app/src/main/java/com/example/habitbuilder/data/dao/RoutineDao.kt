package com.example.habitbuilder.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.habitbuilder.data.entity.RoutineEntity

@Dao
interface RoutineDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(routine: RoutineEntity) : Long

    @Update
    suspend fun update(routine: RoutineEntity)

    @Query("SELECT * FROM routines ORDER BY triggerTime ASC")
    suspend fun getAll(): List<RoutineEntity>

    @Query("SELECT * FROM routines WHERE id = :id LIMIT 1")
    suspend fun get(id: Int): RoutineEntity?

    @Query("DELETE FROM routines WHERE id = :id")
    suspend fun delete(id: Int)

    @Delete
    suspend fun delete(routine: RoutineEntity)

    @Query("DELETE FROM routines")
    suspend fun deleteAll()
}