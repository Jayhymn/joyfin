package com.wakeupdev.joyfin.utils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import com.wakeupdev.joyfin.R

object AlertUtils {
    private var dialog: Dialog? = null

    fun showCustomDialog(mContext: Context) {
        dialog = Dialog(mContext)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setContentView(R.layout.loading_dialog)
        dialog!!.setCancelable(false)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog!!.show()
    }

    fun cancelCustomDialog() {
        if (dialog?.isShowing == true) {
            dialog?.dismiss()
            dialog = null
        }
    }
}