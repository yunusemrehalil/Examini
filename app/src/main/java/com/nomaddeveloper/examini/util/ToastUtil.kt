package com.nomaddeveloper.examini.util

import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.button.MaterialButton
import com.nomaddeveloper.examini.R
import javax.inject.Inject

class ToastUtil @Inject constructor() {
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun createLogoutErrorDialog(context: Context, onOkClicked: () -> Unit): AlertDialog {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.error_alert_dialog, null)
        val dialogTitle = dialogView.findViewById<TextView>(R.id.error_dialog_title)
        val dialogMessage = dialogView.findViewById<TextView>(R.id.error_dialog_message)
        val dialogButton = dialogView.findViewById<MaterialButton>(R.id.error_dialog_ok_button)

        dialogTitle.text = context.getString(R.string.profile_not_found)
        dialogMessage.text = context.getString(R.string.logging_out_message)

        val builder = AlertDialog.Builder(context)
        builder.setView(dialogView)
        builder.setCancelable(false)
        val dialog = builder.create()

        dialogButton.setOnClickListener {
            onOkClicked()
            dialog.dismiss()
        }
        return dialog
    }

    fun showErrorDialogTopicNotFound(
        fragment: Fragment,
        fragmentManager: FragmentManager,
        dialogMessageText: String
    ) {
        val activity = fragment.requireActivity()
        val dialogView = LayoutInflater.from(activity).inflate(R.layout.error_alert_dialog, null)
        val dialogTitle = dialogView.findViewById<TextView>(R.id.error_dialog_title)
        val dialogMessage = dialogView.findViewById<TextView>(R.id.error_dialog_message)
        val dialogButton = dialogView.findViewById<MaterialButton>(R.id.error_dialog_ok_button)

        dialogTitle.text = activity.getString(R.string.error)
        dialogMessage.text = dialogMessageText

        val builder = AlertDialog.Builder(activity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        val dialog = builder.create()

        dialogButton.setOnClickListener {
            dialog.dismiss()
            fragmentManager.beginTransaction().setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out_left
            ).remove(fragment).commit()
            activity.finish()
        }
        dialog.show()
    }

    fun showErrorDialogQuestionNotFound(
        fragment: Fragment,
        fragmentManager: FragmentManager,
        dialogMessageText: String
    ) {
        val context = fragment.requireContext()
        val dialogView = LayoutInflater.from(context).inflate(R.layout.error_alert_dialog, null)
        val dialogTitle = dialogView.findViewById<TextView>(R.id.error_dialog_title)
        val dialogMessage = dialogView.findViewById<TextView>(R.id.error_dialog_message)
        val dialogButton = dialogView.findViewById<MaterialButton>(R.id.error_dialog_ok_button)

        dialogTitle.text = context.getString(R.string.error)
        dialogMessage.text = dialogMessageText

        val builder = AlertDialog.Builder(context)
        builder.setView(dialogView)
        builder.setCancelable(false)
        val dialog = builder.create()

        dialogButton.setOnClickListener {
            dialog.dismiss()
            fragmentManager.beginTransaction().setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out_left
            ).remove(fragment).commit()
            fragmentManager.popBackStack()
        }
        dialog.show()
    }
}