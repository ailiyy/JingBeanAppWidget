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
class MyMenuDialogAdapter(private val mActivity: Activity, var onItemClickListener: MenuDialog.OnItemClickListener) : RecyclerView.Adapter<MyMenuDialogAdapter.MenuItem>() {
    var dataList: ArrayList<String>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyMenuDialogAdapter.MenuItem {
        return MenuItem(LayoutInflater.from(mActivity).inflate(R.layout.menu_item, parent, false))
    }

    override fun onBindViewHolder(holder: MyMenuDialogAdapter.MenuItem, position: Int) {
        holder.title.text = dataList?.get(position)
        holder.title.setOnClickListener {
            onItemClickListener.itemClick(dataList?.get(holder.adapterPosition)!!)
        }
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