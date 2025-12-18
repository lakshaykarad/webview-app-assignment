package com.example.learning_basics.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "my_urls")
data class UserUrl(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    val url : String,
    val timestamp : Long = System.currentTimeMillis()
)