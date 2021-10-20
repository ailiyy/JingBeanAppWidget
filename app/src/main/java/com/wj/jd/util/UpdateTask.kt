package com.wj.jd.util

import android.os.Handler
import android.os.Looper
import com.wj.jd.MainActivity
import com.wj.jd.widget.WidgetUpdateDataUtil

/**
 * author wangjing
 * Date 2021/10/18
 * Description
 */
object UpdateTask {
    var widgetUpdateDataUtil1 = WidgetUpdateDataUtil()
    var widgetUpdateDataUtil2 = WidgetUpdateDataUtil()
    var widgetUpdateDataUtil3 = WidgetUpdateDataUtil()

    var handler = Handler(Looper.getMainLooper())

    fun updateAll() {
        handler.post {
            widgetUpdateDataUtil1.updateWidget("ck")
        }
        handler.postDelayed({
            widgetUpdateDataUtil2.updateWidget("ck1")
        }, 3000)

        handler.postDelayed({
            widgetUpdateDataUtil3.updateWidget("ck2")
        }, 6000)
    }
}