package com.example.karam.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String? = null,
    val duration: Int, // مدت زمان به دقیقه
    val date: Date,
    val categoryId: Long
) 