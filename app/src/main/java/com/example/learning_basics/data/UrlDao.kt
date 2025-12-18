package com.example.learning_basics.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface UrlDao {
    /*Insert Data*/
    @Insert
    suspend fun insertUrl(url: UserUrl)
    @Query("SELECT * FROM my_urls ORDER BY timestamp DESC")
    suspend fun getAllUrls() : List<UserUrl>
    @Query("DELETE FROM my_urls")
    suspend fun clearHistory()

}