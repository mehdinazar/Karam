package com.example.karam.ui.history

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.karam.data.AppDatabase
import com.example.karam.data.model.Task
import com.example.karam.util.DateUtils
import kotlinx.coroutines.launch
import java.util.*

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val taskDao = AppDatabase.getInstance(application).taskDao()
    private val categoryDao = AppDatabase.getInstance(application).categoryDao()

    private val _allTasks = MutableLiveData<List<Task>>()
    val allTasks: LiveData<List<Task>> = _allTasks

    private val _weeklyTasks = MutableLiveData<List<Task>>()
    val weeklyTasks: LiveData<List<Task>> = _weeklyTasks

    private val _monthlyTasks = MutableLiveData<List<Task>>()
    val monthlyTasks: LiveData<List<Task>> = _monthlyTasks

    val categories = categoryDao.getAllCategories()

    init {
        loadTasks()
    }

    fun loadTasks() {
        val tasks = taskDao.getAllTasks()
        _allTasks.value = tasks

        val currentDate = Date()
        val persianDate = DateUtils.toPersianDate(currentDate)
        
        // فیلتر کردن کارهای هفته جاری
        val weekStart = DateUtils.toGregorianDate(persianDate.year, persianDate.month, 1)
        val weekEnd = DateUtils.toGregorianDate(persianDate.year, persianDate.month, persianDate.day)
        _weeklyTasks.value = tasks.filter { it.date in weekStart..weekEnd }

        // فیلتر کردن کارهای ماه جاری
        val monthStart = DateUtils.toGregorianDate(persianDate.year, persianDate.month, 1)
        val monthEnd = DateUtils.toGregorianDate(persianDate.year, persianDate.month + 1, 0)
        _monthlyTasks.value = tasks.filter { it.date in monthStart..monthEnd }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskDao.delete(task)
            loadTasks()
        }
    }
} 