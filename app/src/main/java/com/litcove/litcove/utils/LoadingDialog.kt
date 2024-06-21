package com.litcove.litcove.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.Window
import com.airbnb.lottie.LottieAnimationView
import com.litcove.litcove.R

class LoadingDialog(context: Context) {

    @SuppressLint("InflateParams")
    private val dialog = Dialog(context).apply {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        setContentView(LayoutInflater.from(context).inflate(R.layout.dialog_loading, null))
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private val animationView: LottieAnimationView = dialog.findViewById(R.id.loading_animation)

    fun startLoading() {
        animationView.playAnimation()
        dialog.show()
    }

    fun stopLoading() {
        animationView.cancelAnimation()
        dialog.dismiss()
    }
}