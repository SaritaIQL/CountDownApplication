package com.example.countdownapplication.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments_data_table")
data class Comments (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "commentId")
    var commentid: Int,

    @ColumnInfo(name = "postId")
    var postId: Int,

    @ColumnInfo(name = "id")
    var id : Int,

    @ColumnInfo(name = "name")
    var name : String,
    @ColumnInfo(name = "email")
    var email : String,
    @ColumnInfo(name = "body")
    var body : String

    )