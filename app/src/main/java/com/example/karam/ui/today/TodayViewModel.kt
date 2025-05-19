package com.example.karam.ui.today

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.karam.data.AppDatabase
import com.example.karam.data.model.Task
import kotlinx.coroutines.launch
import java.util.Date

class TodayViewModel(application: Application) : AndroidViewModel(application) {
    private val taskDao = AppDatabase.getDatabase(application).taskDao()
    private val categoryDao = AppDatabase.getDatabase(application).categoryDao()

    val todayTasks: LiveData<List<Task>> = taskDao.getTasksByDate(Date())
    val categories = categoryDao.getAllCategories()

    fun addTask(task: Task) {
        viewModelScope.launch {
            taskDao.insert(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskDao.update(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.delete(task)
        }
    }
} 