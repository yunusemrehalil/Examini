package com.nomaddeveloper.examini.listener

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.nomaddeveloper.examini.util.CountDownTimerUtil
import com.nomaddeveloper.examini.util.ToastUtil.Companion.showErrorDialogQuestionNotFound

class GlideErrorListener(
    private val context: Context,
    private val fragmentManager: FragmentManager,
    private val fragmentTag: String,
    private val errorMessage: String,
    private val countDownTimerUtil: CountDownTimerUtil
) : RequestListener<Drawable> {

    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>,
        isFirstResource: Boolean
    ): Boolean {
        countDownTimerUtil.destroyTimer()
        showErrorDialogQuestionNotFound(context, fragmentManager, fragmentTag, errorMessage)
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
