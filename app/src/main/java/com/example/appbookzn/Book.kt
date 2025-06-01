package com.example.appbookzn

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    val title: String,
    val author: String,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
