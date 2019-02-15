package com.syouth.basicrss.app

import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.squareup.moshi.Moshi
import com.syouth.basicrss.R
import com.syouth.basicrss.backend.ResponseEntity

class Adapter : RecyclerView.Adapter<Adapter.ViewHolder>() {

    var cursor: Cursor? = null
        set(cursor) {
            field = cursor
            notifyDataSetChanged()
        }

    private val moshi = Moshi.Builder().build().adapter(ResponseEntity::class.java)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false) as TextView
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = cursor?.count ?: 0

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        cursor!!.moveToPosition(position)
        vh.item.text = moshi.fromJson(cursor!!.getString(0))?.title
    }

    class ViewHolder(val item: TextView) : RecyclerView.ViewHolder(item)
}
