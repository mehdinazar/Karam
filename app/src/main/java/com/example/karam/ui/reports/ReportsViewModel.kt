package com.example.karam.ui.reports

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.karam.data.AppDatabase
import com.example.karam.data.model.Task
import com.example.karam.util.DateUtils
import java.util.*

class ReportsViewModel(application: Application) : AndroidViewModel(application) {
    private val taskDao = AppDatabase.getInstance(application).taskDao()
    private val categoryDao = AppDatabase.getInstance(application).categoryDao()

    private val _tasks = MutableLiveData<List<Task>>()
    val tasks: LiveData<List<Task>> = _tasks

    private val _totalTime = MutableLiveData<Int>()
    val totalTime: LiveData<Int> = _totalTime

    private val _categoryTimes = MutableLiveData<Map<String, Int>>()
    val categoryTimes: LiveData<Map<String, Int>> = _categoryTimes

    init {
        loadTasks()
    }

    fun loadTasks() {
        val tasks = taskDao.getAllTasks()
        _tasks.value = tasks

        // محاسبه کل زمان
        _totalTime.value = tasks.sumOf { it.timeSpent }

        // محاسبه زمان هر دسته‌بندی
        val categoryTimes = mutableMapOf<String, Int>()
        tasks.forEach { task ->
            val category = categoryDao.getCategory(task.categoryId)
            category?.let {
                categoryTimes[it.name] = (categoryTimes[it.name] ?: 0) + task.timeSpent
            }
        }
        _categoryTimes.value = categoryTimes
    }

    fun getTasksByDateRange(startDate: Date, endDate: Date) {
        val tasks = taskDao.getAllTasks().filter { it.date in startDate..endDate }
        _tasks.value = tasks

        // محاسبه کل زمان
        _totalTime.value = tasks.sumOf { it.timeSpent }

        // محاسبه زمان هر دسته‌بندی
        val categoryTimes = mutableMapOf<String, Int>()
        tasks.forEach { task ->
            val category = categoryDao.getCategory(task.categoryId)
            category?.let {
                categoryTimes[it.name] = (categoryTimes[it.name] ?: 0) + task.timeSpent
            }
        }
        _categoryTimes.value = categoryTimes
    }

    fun getCategoryData(tasks: List<Task>): Map<String, Long> {
        val categoryData = mutableMapOf<String, Long>()
        tasks.forEach { task ->
            val category = categoryDao.getCategoryById(task.categoryId)
            category?.let {
                categoryData[it.name] = (categoryData[it.name] ?: 0) + task.duration
            }
        }
        return categoryData
    }

    fun getTimeData(tasks: List<Task>): Map<String, Long> {
        val timeData = mutableMapOf<String, Long>()
        tasks.forEach { task ->
            val category = categoryDao.getCategoryById(task.categoryId)
            category?.let {
                timeData[it.name] = (timeData[it.name] ?: 0) + task.duration
            }
        }
        return timeData
    }

    fun getTrendData(tasks: List<Task>): Map<Date, Long> {
        val trendData = mutableMapOf<Date, Long>()
        tasks.forEach { task ->
            trendData[task.date] = (trendData[task.date] ?: 0) + task.duration
        }
        return trendData
    }
} 