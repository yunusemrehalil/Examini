package com.nomaddeveloper.examini.util

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.FragmentManager
import com.google.android.material.button.MaterialButton
import com.nomaddeveloper.examini.R

class ToastUtil {
    companion object {
        fun showToast(context: Context, message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

        fun createLogoutErrorDialog(context: Context, onOkClicked: () -> Unit): AlertDialog {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.error_alert_dialog, null)
            val dialogTitle = dialogView.findViewById<TextView>(R.id.error_dialog_title)
            val dialogMessage = dialogView.findViewById<TextView>(R.id.error_dialog_message)
            val dialogButton = dialogView.findViewById<MaterialButton>(R.id.error_dialog_ok_button)

            dialogTitle.text = "Profiliniz bulunamadı..."
            dialogMessage.text = "Çıkış yapılıyor. Devam etmek için butona basınız."

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
            context: Context,
            fragmentManager: FragmentManager,
            fragmentTag: String,
            dialogMessageText: String
        ) {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.error_alert_dialog, null)
            val dialogTitle = dialogView.findViewById<TextView>(R.id.error_dialog_title)
            val dialogMessage = dialogView.findViewById<TextView>(R.id.error_dialog_message)
            val dialogButton = dialogView.findViewById<MaterialButton>(R.id.error_dialog_ok_button)

            dialogTitle.text = "Hata!"
            dialogMessage.text = dialogMessageText

            val builder = AlertDialog.Builder(context)
            builder.setView(dialogView)
            builder.setCancelable(false)
            val dialog = builder.create()

            dialogButton.setOnClickListener {
                dialog.dismiss()
                val fragment = fragmentManager.findFragmentByTag(fragmentTag)
                if (fragment != null) {
                    fragmentManager.beginTransaction().setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.fade_out,
                        R.anim.fade_in,
                        R.anim.slide_out_left
                    ).remove(fragment).commit()
                }
                (context as? Activity)?.finish()
            }
            dialog.show()
        }

        fun showErrorDialogQuestionNotFound(
            context: Context,
            fragmentManager: FragmentManager,
            fragmentTag: String,
            dialogMessageText: String
        ) {
            val dialogView = LayoutInflater.from(context).inflate(R.layout.error_alert_dialog, null)
            val dialogTitle = dialogView.findViewById<TextView>(R.id.error_dialog_title)
            val dialogMessage = dialogView.findViewById<TextView>(R.id.error_dialog_message)
            val dialogButton = dialogView.findViewById<MaterialButton>(R.id.error_dialog_ok_button)

            dialogTitle.text = "Hata!"
            dialogMessage.text = dialogMessageText

            val builder = AlertDialog.Builder(context)
            builder.setView(dialogView)
            builder.setCancelable(false)
            val dialog = builder.create()

            dialogButton.setOnClickListener {
                dialog.dismiss()
                val fragment = fragmentManager.findFragmentByTag(fragmentTag)
                if (fragment != null) {
                    fragmentManager.beginTransaction().setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.fade_out,
                        R.anim.fade_in,
                        R.anim.slide_out_left
                    ).remove(fragment).commit()
                }
                fragmentManager.popBackStack()
            }
            dialog.show()
        }
    }
}