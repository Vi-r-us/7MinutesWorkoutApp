package com.example.a7minutesworkout

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Insert
    suspend fun insert(historyEntity: HistoryEntity)

    @Delete
    suspend fun delete(historyEntity: HistoryEntity)

    @Query("SELECT * FROM `history-table`")
    fun fetchAllHistory(): Flow<List<HistoryEntity>>

    @Query("DELETE FROM `history-table`")
    suspend fun deleteAllHistory()

}