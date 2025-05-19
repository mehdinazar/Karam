package com.example.karam.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.karam.data.AppDatabase
import com.example.karam.data.model.Category

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getInstance(application)
    private val categoryDao = database.categoryDao()

    private val _categories = MutableLiveData<List<Category>>()
    val categories: LiveData<List<Category>> = _categories

    init {
        loadCategories()
    }

    private fun loadCategories() {
        _categories.value = categoryDao.getAllCategories()
    }

    fun addCategory(name: String) {
        val category = Category(name = name)
        categoryDao.insertCategory(category)
        loadCategories()
    }

    fun updateCategory(category: Category) {
        categoryDao.updateCategory(category)
        loadCategories()
    }

    fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
        loadCategories()
    }
} 