package com.nomaddeveloper.examini.ui.loading

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.nomaddeveloper.examini.R
import com.nomaddeveloper.examini.databinding.FragmentLoadingDialogBinding

class LoadingDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentLoadingDialogBinding
    private var isShowing: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false
        setStyle(STYLE_NO_TITLE, R.style.LoadingDialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoadingDialogBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        isShowing = true
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            super.show(manager, tag)
            isShowing = true
        } catch (e: Exception) {
            // ignored
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        try {
            isShowing = false
            super.onDismiss(dialog)
        } catch (e: Exception) {
            // ignored
        }
    }

    override fun dismissAllowingStateLoss() {
        try {
            isShowing = false
            super.dismissAllowingStateLoss()
        } catch (e: Exception) {
            // ignored
        }
    }

    fun isDialogShowing(): Boolean {
        return isShowing
    }
}