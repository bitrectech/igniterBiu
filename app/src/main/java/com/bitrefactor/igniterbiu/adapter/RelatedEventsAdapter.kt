package com.bitrefactor.igniterbiu.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bitrefactor.igniterbiu.R
import com.bitrefactor.igniterbiu.data.bean.Evt

class RelatedEventsAdapter(context: Context, evt: ArrayList<Evt>) :
    RecyclerView.Adapter<RelatedEventsAdapter.ViewHolder>() {

    private val evt: ArrayList<Evt>
    private val inflater: LayoutInflater
    private val context: Context
    private var previousPosition = 0

    init {
        this.evt = evt
        inflater = LayoutInflater.from(context)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.item_about, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("MissingPermission", "UseCompatLoadingForDrawables")
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val evt = evt[position]
        holder.title.setText(evt.title)
        holder.image.setImageDrawable(context.resources.getDrawable(evt.image))
        holder.itemView.setOnClickListener { evt.evt?.invoke() }
        previousPosition = position
    }

    override fun getItemCount(): Int {
        return if (evt.isNotEmpty()) {
            evt.size
        } else {
            0
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.evt_bind_title)
        var image: ImageView = view.findViewById(R.id.evt_bind_ico)
    }
}