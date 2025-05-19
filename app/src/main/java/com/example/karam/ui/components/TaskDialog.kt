package com.example.karam.ui.components

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.karam.R
import com.example.karam.data.model.Task
import com.example.karam.databinding.DialogTaskBinding
import com.example.karam.ui.main.MainViewModel
import com.example.karam.util.DateUtils
import java.util.*

class TaskDialog : DialogFragment() {
    private var _binding: DialogTaskBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private var task: Task? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.Theme_MaterialComponents_Dialog_MinWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupObservers()
    }

    private fun setupViews() {
        // تنظیم عنوان دیالوگ
        binding.titleText.text = if (task == null) "افزودن کار جدید" else "ویرایش کار"

        // تنظیم دکمه‌ها
        binding.cancelButton.setOnClickListener { dismiss() }
        binding.saveButton.setOnClickListener { saveTask() }

        // تنظیم فیلد تاریخ
        binding.dateField.setOnClickListener {
            showDatePicker()
        }

        // نمایش تاریخ فعلی یا تاریخ کار
        val currentDate = task?.date ?: Date()
        binding.dateField.setText(DateUtils.formatPersianDate(currentDate))

        // پر کردن فیلدهای دیگر اگر در حالت ویرایش هستیم
        task?.let {
            binding.titleField.setText(it.title)
            binding.descriptionField.setText(it.description)
            binding.timeField.setText(it.timeSpent.toString())
        }
    }

    private fun setupObservers() {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categories.map { it.name }
            )
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.categorySpinner.adapter = adapter

            // انتخاب دسته‌بندی فعلی در حالت ویرایش
            task?.let { task ->
                val position = categories.indexOfFirst { it.id == task.categoryId }
                if (position != -1) {
                    binding.categorySpinner.setSelection(position)
                }
            }
        }
    }

    private fun showDatePicker() {
        val currentDate = task?.date ?: Date()
        val persianDate = DateUtils.toPersianDate(currentDate)
        
        PersianDatePickerDialog.newInstance(persianDate) { year, month, day ->
            val newDate = DateUtils.toGregorianDate(year, month, day)
            binding.dateField.setText(DateUtils.formatPersianDate(newDate))
        }.show(childFragmentManager, "date_picker")
    }

    private fun saveTask() {
        val title = binding.titleField.text.toString()
        val description = binding.descriptionField.text.toString()
        val timeSpent = binding.timeField.text.toString().toIntOrNull() ?: 0
        val categoryId = viewModel.categories.value?.get(binding.categorySpinner.selectedItemPosition)?.id ?: return
        val date = DateUtils.fromPersianDateString(binding.dateField.text.toString()) ?: Date()

        if (title.isBlank()) {
            binding.titleField.error = "عنوان نمی‌تواند خالی باشد"
            return
        }

        if (task == null) {
            // ایجاد کار جدید
            val newTask = Task(
                title = title,
                description = description,
                timeSpent = timeSpent,
                categoryId = categoryId,
                date = date
            )
            viewModel.addTask(newTask)
        } else {
            // ویرایش کار موجود
            task = task?.copy(
                title = title,
                description = description,
                timeSpent = timeSpent,
                categoryId = categoryId,
                date = date
            )
            task?.let { viewModel.updateTask(it) }
        }

        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(task: Task? = null): TaskDialog {
            return TaskDialog().apply {
                this.task = task
            }
        }
    }
} 