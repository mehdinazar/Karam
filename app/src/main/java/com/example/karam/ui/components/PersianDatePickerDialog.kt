package com.example.karam.ui.components

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import com.example.karam.R
import com.example.karam.util.DateUtils
import com.google.android.material.button.MaterialButton
import java.util.*

class PersianDatePickerDialog : DialogFragment() {
    private var onDateSelectedListener: ((Date) -> Unit)? = null
    private var initialDate: Date = Date()

    private lateinit var yearPicker: NumberPicker
    private lateinit var monthPicker: NumberPicker
    private lateinit var dayPicker: NumberPicker

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return Dialog(requireContext(), R.style.Theme_MaterialComponents_Dialog_MinWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.dialog_persian_date_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        yearPicker = view.findViewById(R.id.year_picker)
        monthPicker = view.findViewById(R.id.month_picker)
        dayPicker = view.findViewById(R.id.day_picker)

        setupPickers()
        setupButtons(view)
    }

    private fun setupPickers() {
        val persianDate = DateUtils.toPersianDate(initialDate)
        val currentYear = persianDate.year
        val currentMonth = persianDate.month
        val currentDay = persianDate.day

        // تنظیم سال‌ها
        yearPicker.minValue = currentYear - 10
        yearPicker.maxValue = currentYear + 10
        yearPicker.value = currentYear

        // تنظیم ماه‌ها
        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.value = currentMonth
        monthPicker.displayedValues = arrayOf(
            "فروردین", "اردیبهشت", "خرداد",
            "تیر", "مرداد", "شهریور",
            "مهر", "آبان", "آذر",
            "دی", "بهمن", "اسفند"
        )

        // تنظیم روزها
        updateDayPicker(currentYear, currentMonth, currentDay)

        // به‌روزرسانی روزها با تغییر سال یا ماه
        yearPicker.setOnValueChangedListener { _, _, _ ->
            updateDayPicker(yearPicker.value, monthPicker.value, dayPicker.value)
        }

        monthPicker.setOnValueChangedListener { _, _, _ ->
            updateDayPicker(yearPicker.value, monthPicker.value, dayPicker.value)
        }
    }

    private fun updateDayPicker(year: Int, month: Int, currentDay: Int) {
        val persianDate = com.github.samanzamani.persiandate.PersianDate()
        persianDate.year = year
        persianDate.month = month
        val daysInMonth = persianDate.monthDays

        dayPicker.minValue = 1
        dayPicker.maxValue = daysInMonth
        dayPicker.value = if (currentDay > daysInMonth) daysInMonth else currentDay
    }

    private fun setupButtons(view: View) {
        view.findViewById<MaterialButton>(R.id.cancel_button).setOnClickListener {
            dismiss()
        }

        view.findViewById<MaterialButton>(R.id.ok_button).setOnClickListener {
            val persianDate = com.github.samanzamani.persiandate.PersianDate()
            persianDate.year = yearPicker.value
            persianDate.month = monthPicker.value
            persianDate.day = dayPicker.value
            onDateSelectedListener?.invoke(persianDate.toDate())
            dismiss()
        }
    }

    companion object {
        fun newInstance(
            initialDate: Date = Date(),
            onDateSelected: (Date) -> Unit
        ): PersianDatePickerDialog {
            return PersianDatePickerDialog().apply {
                this.initialDate = initialDate
                this.onDateSelectedListener = onDateSelected
            }
        }
    }
} 