package com.example.karam.ui.today

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.example.karam.R
import com.example.karam.data.model.Category
import com.example.karam.data.model.Task
import com.example.karam.databinding.DialogAddEditTaskBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.Date

class AddEditTaskDialog : DialogFragment() {
    private var _binding: DialogAddEditTaskBinding? = null
    private val binding get() = _binding!!
    private var task: Task? = null
    private var categories: List<Category> = emptyList()
    private var onTaskSaved: ((Task) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddEditTaskBinding.inflate(LayoutInflater.from(context))

        setupCategoryDropdown()
        setupInitialValues()

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (task == null) "افزودن کار جدید" else "ویرایش کار")
            .setView(binding.root)
            .setPositiveButton("ذخیره") { _, _ ->
                saveTask()
            }
            .setNegativeButton("انصراف", null)
            .create()
    }

    private fun setupCategoryDropdown() {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            categories.map { it.name }
        )
        binding.categoryDropdown.setAdapter(adapter)
    }

    private fun setupInitialValues() {
        task?.let { task ->
            binding.titleEditText.setText(task.title)
            binding.descriptionEditText.setText(task.description)
            binding.durationEditText.setText(task.duration.toString())
            binding.categoryDropdown.setText(
                categories.find { it.id == task.categoryId }?.name ?: ""
            )
        }
    }

    private fun saveTask() {
        val title = binding.titleEditText.text.toString()
        val description = binding.descriptionEditText.text.toString()
        val duration = binding.durationEditText.text.toString().toIntOrNull() ?: 0
        val categoryName = binding.categoryDropdown.text.toString()
        val category = categories.find { it.name == categoryName }

        if (title.isBlank() || duration <= 0 || category == null) {
            return
        }

        val newTask = task?.copy(
            title = title,
            description = description,
            duration = duration,
            categoryId = category.id
        ) ?: Task(
            title = title,
            description = description,
            duration = duration,
            date = Date(),
            categoryId = category.id
        )

        onTaskSaved?.invoke(newTask)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(
            task: Task? = null,
            categories: List<Category>,
            onTaskSaved: (Task) -> Unit
        ): AddEditTaskDialog {
            return AddEditTaskDialog().apply {
                this.task = task
                this.categories = categories
                this.onTaskSaved = onTaskSaved
            }
        }
    }
} 