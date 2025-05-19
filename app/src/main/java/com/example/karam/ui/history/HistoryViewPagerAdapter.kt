package com.example.karam.ui.history

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.karam.data.model.Task

class HistoryViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    private var allTasks: List<Task> = emptyList()
    private var weeklyTasks: List<Task> = emptyList()
    private var monthlyTasks: List<Task> = emptyList()

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HistoryListFragment.newInstance(allTasks)
            1 -> HistoryListFragment.newInstance(weeklyTasks)
            2 -> HistoryListFragment.newInstance(monthlyTasks)
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }

    fun updateAllTasks(tasks: List<Task>) {
        allTasks = tasks
        notifyDataSetChanged()
    }

    fun updateWeeklyTasks(tasks: List<Task>) {
        weeklyTasks = tasks
        notifyDataSetChanged()
    }

    fun updateMonthlyTasks(tasks: List<Task>) {
        monthlyTasks = tasks
        notifyDataSetChanged()
    }
} 