package com.example.karam.ui.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.karam.databinding.FragmentTodayBinding

class TodayFragment : Fragment() {
    private var _binding: FragmentTodayBinding? = null
    private val binding get() = _binding!!
    private val viewModel: TodayViewModel by viewModels()
    private lateinit var adapter: TaskAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        adapter = TaskAdapter { task ->
            showAddEditTaskDialog(task)
        }
        binding.tasksRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@TodayFragment.adapter
        }
    }

    private fun setupObservers() {
        viewModel.todayTasks.observe(viewLifecycleOwner) { tasks ->
            adapter.submitList(tasks)
            binding.emptyView.visibility = if (tasks.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupClickListeners() {
        binding.fabAddTask.setOnClickListener {
            showAddEditTaskDialog()
        }
    }

    private fun showAddEditTaskDialog(task: Task? = null) {
        viewModel.categories.value?.let { categories ->
            AddEditTaskDialog.newInstance(
                task = task,
                categories = categories,
                onTaskSaved = { newTask ->
                    if (task == null) {
                        viewModel.addTask(newTask)
                    } else {
                        viewModel.updateTask(newTask)
                    }
                }
            ).show(childFragmentManager, "AddEditTaskDialog")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 