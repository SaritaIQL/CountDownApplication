package com.example.countdownapplication.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentsDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE )
    suspend fun insertSubscriber(subscriber: Comments) : Long

    @Query("SELECT * FROM comments_data_table")
    fun getAllComments(): Flow<List<Comments>>

    @Query("DELETE FROM comments_data_table")
    fun deleteAllRecord()
}