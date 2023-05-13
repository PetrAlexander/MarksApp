package com.example.marksapp.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.marksapp.R
import com.example.marksapp.data.Event

class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    var onEventClickListener: OnEventClickListener? = null

    var eventList: List<Event> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.event_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = eventList.get(position)
        holder.address.text = event.address
        holder.name.text = event.name
        holder.itemView.setOnClickListener {
            onEventClickListener?.onEventClick(event)
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    interface OnEventClickListener {
        fun onEventClick(event: Event)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var address: TextView
        var name: TextView

        init {
            address = itemView.findViewById<TextView>(R.id.tv_address)
            name = itemView.findViewById<TextView>(R.id.tv_name)
        }
    }
}