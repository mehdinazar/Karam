package com.example.karam.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.karam.data.model.Task
import java.util.Date

@Dao
interface TaskDao {
    @Insert
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * FROM tasks WHERE date = :date ORDER BY id DESC")
    fun getTasksByDate(date: Date): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE categoryId = :categoryId ORDER BY date DESC")
    fun getTasksByCategory(categoryId: Long): LiveData<List<Task>>

    @Query("SELECT SUM(duration) FROM tasks WHERE categoryId = :categoryId AND date BETWEEN :startDate AND :endDate")
    fun getTotalDurationByCategoryAndDateRange(categoryId: Long, startDate: Date, endDate: Date): LiveData<Int>

    @Query("SELECT * FROM tasks ORDER BY date DESC")
    fun getAllTasks(): List<Task>
} 