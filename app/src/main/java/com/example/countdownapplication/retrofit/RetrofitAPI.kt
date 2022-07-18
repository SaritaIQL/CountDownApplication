package com.example.countdownapplication.retrofit

import com.example.countdownapplication.retrofit.Response.CommentResponseItem
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitAPI {
    @GET("comments")
    fun getCommentResponse() : Call<ArrayList<CommentResponseItem>>
}