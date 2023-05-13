package com.example.marksapp.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.marksapp.R


class SuggestAdapter :
    RecyclerView.Adapter<SuggestAdapter.ViewHolder>() {

    var onSuggestClickListener: OnSuggestClickListener? = null

    var mData: List<String?> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.suggest_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val suggestText = mData.get(position)
        holder.textView.text = suggestText
        holder.itemView.setOnClickListener {
            onSuggestClickListener?.onSuggestClick(suggestText)
        }
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    interface OnSuggestClickListener {
        fun onSuggestClick(suggest: String?)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView

        init {
            textView = itemView.findViewById<TextView>(R.id.textView)
        }
    }
}