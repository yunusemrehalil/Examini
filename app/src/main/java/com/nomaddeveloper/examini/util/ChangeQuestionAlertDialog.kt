package com.nomaddeveloper.examini.util

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.nomaddeveloper.examini.databinding.AlertDialogChangeQuestionBinding

class ChangeQuestionAlertDialog(context: Context) : AlertDialog(context) {
    private lateinit var binding: AlertDialogChangeQuestionBinding
    private lateinit var levelValueTV: TextView
    private lateinit var timeValueTV: TextView
    private lateinit var okButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AlertDialogChangeQuestionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
    }

    private fun setupUI() {
        binding.apply {
            levelValueTV = tvAlertDialogLevelValue
            timeValueTV = tvAlertDialogTimeValue
            okButton = mbAlertDialogOk
        }
    }

    fun setLevel(levelValue: String) {
        levelValueTV.text = levelValue
    }

    fun setEstimatedTime(estimatedSolvingTime: String) {
        timeValueTV.text = estimatedSolvingTime
    }

    fun setChangeQuestionButtonListener(listener: View.OnClickListener) {
        okButton.setOnClickListener(listener)
    }
}