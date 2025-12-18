package com.example.learning_basics.Repository

import android.util.Log
import com.example.learning_basics.data.UrlDao
import com.example.learning_basics.data.UserUrl
import com.example.learning_basics.service.ApiService
import retrofit2.Retrofit
import javax.inject.Inject


class UrlRepository @Inject constructor(
    private val urlDao: UrlDao,
    private val apiService: ApiService
) {
    // Add url for home screen
    suspend fun addUrl(url : String){
        val urerUrl = UserUrl(url = url)
        return urlDao.insertUrl(urerUrl)
    }
    // Get history for History screen
    suspend fun getHistory() : List<UserUrl>{
        return urlDao.getAllUrls()
    }
    // delete history
    suspend fun clearHistory() {
        return urlDao.clearHistory()
    }

    // upload history using post method
    suspend fun uploadHistory() : Result<String>{
        try {
            val history = urlDao.getAllUrls()
            if (history.isEmpty()){
                return Result.failure(Exception("No History to upload"))
            }
            // apiService use to upload history to server
            apiService.uploadHistory(history)
            return Result.success("History upload successfully")
        }catch (e : Exception){
            return Result.failure(Exception(e.message?:"Somthing went wrong"))
        }
    }
}