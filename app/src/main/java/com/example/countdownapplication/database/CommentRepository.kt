package com.example.countdownapplication.database

class CommentRepository (private  val dao: CommentsDAO){
    val comments = dao.getAllComments()

    suspend fun insert(comments : Comments): Long {
        return dao.insertSubscriber(comments)
    }

     fun deleteAllRecord(){
        return dao.deleteAllRecord()
    }

}