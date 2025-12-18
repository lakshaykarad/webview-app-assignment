package com.example.learning_basics.service

import com.example.learning_basics.data.UrlDao
import com.example.learning_basics.data.UserUrl
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {

    // suspend fun to upload history
    @POST("upload")
    suspend fun uploadHistory(@Body history : List<UserUrl>)

}