package com.daryush.thesis

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerAdapter(val items: List<ToTp>, val context: Context) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    // Inflate the layout and return a ViewHolder object
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.totpshow, parent, false)
        return ViewHolder(view)
    }

    // Return the total number of items in the list
    override fun getItemCount(): Int {
        return items.size
    }

    // Bind data to the ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val totp = items[position]
        holder.bind(totp)
    }

    // Inner ViewHolder class to hold item views
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val secretTextView: TextView = itemView.findViewById(R.id.secret)
        private val issuerTextView: TextView = itemView.findViewById(R.id.issuer)
        private val labelTextView: TextView = itemView.findViewById(R.id.label)

        // Bind the data to the views
        fun bind(totp: ToTp) {
            secretTextView.text = totp.secret
            issuerTextView.text = totp.IssuerName
            labelTextView.text = totp.label
        }
    }
}
