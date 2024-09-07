package com.nomaddeveloper.examini.util

import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.nomaddeveloper.examini.ui.question.viewmodel.TimerViewModel

class GlideErrorListener(
    private val fragment: Fragment,
    private val fragmentManager: FragmentManager,
    private val errorMessage: String,
    private val timerViewModel: TimerViewModel,
    private val toastUtil: ToastUtil
) : RequestListener<Drawable> {

    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>,
        isFirstResource: Boolean
    ): Boolean {
        timerViewModel.destroyTimer()
        toastUtil.showErrorDialogQuestionNotFound(
            fragment,
            fragmentManager,
            errorMessage
        )
        return false
    }

    override fun onResourceReady(
        resource: Drawable,
        model: Any,
        target: Target<Drawable>?,
        dataSource: DataSource,
        isFirstResource: Boolean
    ): Boolean {
        // Image loaded successfully, do nothing special
        return false
    }
}
