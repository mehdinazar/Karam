package com.example.karam.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.karam.R
import com.example.karam.databinding.FragmentSettingsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.example.karam.ui.components.AboutDialog

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupObservers()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        categoryAdapter = CategoryAdapter(
            onEditClick = { category ->
                showEditCategoryDialog(category)
            },
            onDeleteClick = { category ->
                showDeleteCategoryDialog(category)
            }
        )

        binding.categoriesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = categoryAdapter
        }
    }

    private fun setupObservers() {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            categoryAdapter.submitList(categories)
            binding.emptyView.visibility = if (categories.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun setupClickListeners() {
        binding.addCategoryButton.setOnClickListener {
            showAddCategoryDialog()
        }

        binding.darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            // TODO: پیاده‌سازی تغییر حالت تاریک
        }

        binding.notificationsSwitch.setOnCheckedChangeListener { _, isChecked ->
            // TODO: پیاده‌سازی تغییر وضعیت اعلان‌ها
        }

        binding.aboutButton.setOnClickListener {
            AboutDialog.newInstance().show(childFragmentManager, "about_dialog")
        }
    }

    private fun showAddCategoryDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_category, null)
        val nameEditText = dialogView.findViewById<TextInputEditText>(R.id.category_name_edit_text)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("افزودن دسته‌بندی جدید")
            .setView(dialogView)
            .setPositiveButton("افزودن") { _, _ ->
                val name = nameEditText.text.toString()
                if (name.isNotEmpty()) {
                    viewModel.addCategory(name)
                }
            }
            .setNegativeButton("انصراف", null)
            .show()
    }

    private fun showEditCategoryDialog(category: Category) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_category, null)
        val nameEditText = dialogView.findViewById<TextInputEditText>(R.id.category_name_edit_text)
        nameEditText.setText(category.name)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("ویرایش دسته‌بندی")
            .setView(dialogView)
            .setPositiveButton("ذخیره") { _, _ ->
                val name = nameEditText.text.toString()
                if (name.isNotEmpty()) {
                    viewModel.updateCategory(category.copy(name = name))
                }
            }
            .setNegativeButton("انصراف", null)
            .show()
    }

    private fun showDeleteCategoryDialog(category: Category) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("حذف دسته‌بندی")
            .setMessage("آیا از حذف این دسته‌بندی اطمینان دارید؟")
            .setPositiveButton("بله") { _, _ ->
                viewModel.deleteCategory(category)
            }
            .setNegativeButton("خیر", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 