package com.example.countdownapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Comments::class], version = 1)
abstract class CommentDatabase : RoomDatabase() {
    abstract val commentsDAO : CommentsDAO

    companion object {
        @Volatile
        private var INSTANCE: CommentDatabase? = null
        fun getInstance(context: Context): CommentDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CommentDatabase::class.java,
                        "comments_data_table"
                    ).build()
                }
                return instance
            }
        }
    }
}