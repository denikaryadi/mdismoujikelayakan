package com.komida.co.id.mdisujikelayakan.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
class CustomDialogIntent(
    private val activity: Activity,
    private val title: String,
    private val message: String,
    private val intent: Intent
) {
    private var alertDialog: AlertDialog? = null

    init {
        if (!activity.isFinishing) {
            alertDialog = AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                    activity.startActivity(intent) // Start the intent when "OK" is clicked
                    activity.finish() // Finish the activity
                }
                .create()
        }
    }

    fun show() {
        if (!activity.isFinishing && alertDialog != null) {
            alertDialog!!.show()

        }
    }

    fun setButtonClickListener(onClickListener: DialogInterface.OnClickListener) {
        alertDialog?.setButton(AlertDialog.BUTTON_POSITIVE, "OK", onClickListener)
    }
}