package com.wj.jd.dialog

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wj.jd.R

/**
 * author wangjing
 * Date 2021/10/21
 * Description
 */
class MyMenuDialogAdapter(private val mActivity: Activity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var dataList: ArrayList<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MenuItem(LayoutInflater.from(mActivity).inflate(R.layout.menu_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    }

    override fun getItemCount(): Int {
        return if (dataList == null) {
            0
        } else {
            dataList!!.size
        }
    }

    inner class MenuItem(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
    }
}