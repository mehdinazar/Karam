package com.example.karam.ui.reports

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.karam.databinding.FragmentReportPageBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import java.text.SimpleDateFormat
import java.util.*

class ReportPageFragment : Fragment() {
    private var _binding: FragmentReportPageBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ReportsViewModel by activityViewModels()

    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            position = it.getInt(ARG_POSITION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {
        when (position) {
            0 -> viewModel.dailyTasks.observe(viewLifecycleOwner) { tasks ->
                updateCharts(tasks)
            }
            1 -> viewModel.weeklyTasks.observe(viewLifecycleOwner) { tasks ->
                updateCharts(tasks)
            }
            2 -> viewModel.monthlyTasks.observe(viewLifecycleOwner) { tasks ->
                updateCharts(tasks)
            }
        }
    }

    private fun updateCharts(tasks: List<Task>) {
        if (tasks.isEmpty()) {
            binding.categoryChart.visibility = View.GONE
            binding.timeChart.visibility = View.GONE
            binding.trendChart.visibility = View.GONE
            return
        }

        binding.categoryChart.visibility = View.VISIBLE
        binding.timeChart.visibility = View.VISIBLE
        binding.trendChart.visibility = View.VISIBLE

        // به‌روزرسانی نمودار دایره‌ای دسته‌بندی‌ها
        updateCategoryChart(tasks)

        // به‌روزرسانی نمودار میله‌ای زمان
        updateTimeChart(tasks)

        // به‌روزرسانی نمودار خطی روند
        updateTrendChart(tasks)
    }

    private fun updateCategoryChart(tasks: List<Task>) {
        val categoryData = viewModel.getCategoryData(tasks)
        val entries = categoryData.map { PieEntry(it.value.toFloat(), it.key) }

        val dataSet = PieDataSet(entries, "دسته‌بندی‌ها")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueFormatter = PercentFormatter(binding.categoryChart)
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.WHITE

        val data = PieData(dataSet)
        binding.categoryChart.data = data
        binding.categoryChart.description.isEnabled = false
        binding.categoryChart.legend.isEnabled = true
        binding.categoryChart.setEntryLabelTextSize(12f)
        binding.categoryChart.setEntryLabelColor(Color.BLACK)
        binding.categoryChart.invalidate()
    }

    private fun updateTimeChart(tasks: List<Task>) {
        val timeData = viewModel.getTimeData(tasks)
        val entries = timeData.map { BarEntry(timeData.keys.indexOf(it.key).toFloat(), it.value.toFloat()) }

        val dataSet = BarDataSet(entries, "زمان (دقیقه)")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

        val data = BarData(dataSet)
        binding.timeChart.data = data
        binding.timeChart.description.isEnabled = false
        binding.timeChart.legend.isEnabled = true
        binding.timeChart.xAxis.valueFormatter = IndexAxisValueFormatter(timeData.keys.toList())
        binding.timeChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.timeChart.xAxis.granularity = 1f
        binding.timeChart.xAxis.setDrawGridLines(false)
        binding.timeChart.axisLeft.axisMinimum = 0f
        binding.timeChart.axisRight.isEnabled = false
        binding.timeChart.invalidate()
    }

    private fun updateTrendChart(tasks: List<Task>) {
        val trendData = viewModel.getTrendData(tasks)
        val entries = trendData.map { LineEntry(trendData.keys.indexOf(it.key).toFloat(), it.value.toFloat()) }

        val dataSet = LineDataSet(entries, "روند زمانی")
        dataSet.color = Color.BLUE
        dataSet.setCircleColor(Color.BLUE)
        dataSet.lineWidth = 2f
        dataSet.circleRadius = 4f
        dataSet.setDrawValues(false)

        val data = LineData(dataSet)
        binding.trendChart.data = data
        binding.trendChart.description.isEnabled = false
        binding.trendChart.legend.isEnabled = true
        binding.trendChart.xAxis.valueFormatter = IndexAxisValueFormatter(
            trendData.keys.map { SimpleDateFormat("dd/MM", Locale.getDefault()).format(it) }
        )
        binding.trendChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        binding.trendChart.xAxis.granularity = 1f
        binding.trendChart.xAxis.setDrawGridLines(false)
        binding.trendChart.axisLeft.axisMinimum = 0f
        binding.trendChart.axisRight.isEnabled = false
        binding.trendChart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_POSITION = "position"

        fun newInstance(position: Int) = ReportPageFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_POSITION, position)
            }
        }
    }
}
 