package com.example.karam.ui.components

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.karam.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AboutDialog : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.about)
            .setMessage(R.string.about_message)
            .setPositiveButton(android.R.string.ok, null)
            .create()
    }

    companion object {
        fun newInstance() = AboutDialog()
    }
} 