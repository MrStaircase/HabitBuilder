package com.example.habitbuilder.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.habitbuilder.data.DailyCompletion
import com.example.habitbuilder.data.entity.CompletionEntity
import java.util.Calendar

@Dao
interface CompletionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(completion: CompletionEntity)

    @Query("SELECT * FROM completion ORDER BY routineId ASC, date ASC, actionId ASC")
    suspend fun getAll(): List<CompletionEntity>

    @Query("SELECT * FROM completion where routineId = :routineId")
    suspend fun get(routineId: Int): List<CompletionEntity>

    @Query("SELECT * FROM completion where routineId = :routineId AND date = :date")
    suspend fun get(routineId: Int, date: Calendar): List<CompletionEntity>

    @Query(
        "SELECT date, COUNT(DISTINCT actionId) AS completedActions " +
                "FROM completion " +
                "WHERE routineId = :routineId " +
                "GROUP BY date"
    )
    suspend fun getCountPerDay(routineId: Int): List<DailyCompletion>

    @Delete
    suspend fun delete(completion: CompletionEntity)

    @Query("DELETE FROM completion")
    suspend fun deleteAll()
}