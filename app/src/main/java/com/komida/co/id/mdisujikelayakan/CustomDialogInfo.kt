package id.co.komida.mdismojava

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.komida.co.id.mdisujikelayakan.R

class CustomDialog(context: Context?, title: String?, infod: String?) :
    Dialog(context!!) {
    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_info)
        setCancelable(true)
        val dialog = Dialog(context!!)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE) // before
        dialog.setContentView(R.layout.dialog_info)
        dialog.setCancelable(true)
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(window!!.attributes)
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        (findViewById<View>(R.id.title_info) as TextView).text = title
        (findViewById<View>(R.id.content_info) as TextView).text = infod
        val closeButton = findViewById<AppCompatButton>(R.id.bt_close)
        closeButton.setOnClickListener { dismiss() }

        // Use the appropriate setter methods to set the title and content
        window!!.attributes = lp
    }
}