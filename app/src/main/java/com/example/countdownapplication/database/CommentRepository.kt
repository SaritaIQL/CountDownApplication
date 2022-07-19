package com.example.countdownapplication.database

import android.util.Log

class CommentRepository (private  val dao: CommentsDAO){
    val comments = dao.getAllComments()

    suspend fun insert(comments : Comments): Long {
        return dao.insertSubscriber(comments)
    }

    fun deleteAll(){
        return dao.deleteAllRecord()
    }

}