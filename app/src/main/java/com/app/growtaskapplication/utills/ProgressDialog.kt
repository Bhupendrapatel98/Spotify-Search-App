package com.app.growtaskapplication.utills

import android.app.Dialog
import android.content.Context
import android.view.WindowManager
import com.app.growtaskapplication.R

object ProgressDialog {

    private var progressDialog: Dialog? = null

    fun show(context: Context) {
        dismiss()
        progressDialog = Dialog(context, R.style.TransParentDialog).apply {
            setContentView(R.layout.progress_dialog)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            // window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
            )
        }
        progressDialog?.show()
    }

    fun dismiss() {
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
        progressDialog = null
    }
}