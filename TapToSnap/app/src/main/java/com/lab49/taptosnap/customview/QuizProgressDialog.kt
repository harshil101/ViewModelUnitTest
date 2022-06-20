package com.lab49.taptosnap.customview

import android.app.Dialog
import android.content.Context
import android.view.View
import com.lab49.taptosnap.R

class QuizProgressDialog : Dialog {
    constructor(context: Context) : super(context, R.style.CustomProgressDialog) {
        init()
    }

    constructor(context: Context, themeResId: Int) : super(context, R.style.CustomProgressDialog) {
        init()
    }

    @Suppress("DEPRECATION")
    private fun init() {
        window?.addFlags(
            View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_IMMERSIVE
        )
        setContentView(R.layout.quiz_progress_dialog)
        setCancelable(false)
    }
}