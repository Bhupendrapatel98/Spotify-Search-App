package com.app.growtaskapplication.utills

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.app.growtaskapplication.R

object ProgressDialog {

    private var progressDialog: Dialog? = null

    fun show(context:Context) {
        if (progressDialog == null) {
            progressDialog = Dialog(context).apply {
                setContentView(R.layout.progress_dialog)
                setCancelable(false)
                setCanceledOnTouchOutside(false)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
        progressDialog?.show()
    }

    fun dismiss() {
        progressDialog?.dismiss()
        //progressDialog = null
    }
}