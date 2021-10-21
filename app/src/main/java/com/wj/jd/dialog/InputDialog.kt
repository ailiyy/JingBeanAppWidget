package com.wj.jd.dialog

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wj.jd.R

class InputDialog(var mActivity: Activity) : Dialog(mActivity!!) {

    init {
        initView()
    }

    fun initView(): InputDialog {
        setContentView(R.layout.dialog_layout_style3)
        val divierId = context.resources.getIdentifier("android:id/titleDivider", null, null)
        val divider = findViewById<View>(divierId)
        divider?.setBackgroundColor(Color.TRANSPARENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setLayout(context.resources.displayMetrics.widthPixels * 5 / 6, LinearLayout.LayoutParams.WRAP_CONTENT)
        return this
    }

    fun pop() {
        if (!isShowing) {
            try {
                show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun show() {
        try {
            super.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}