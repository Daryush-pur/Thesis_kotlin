package com.daryush.thesis

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import dev.turingcomplete.kotlinonetimepassword.GoogleAuthenticator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class RecyclerAdapter(
    val items: MutableList<ToTp>,
    val context: Context,
    val database: ToTpDataBase,
    val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

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

    // Method to delete an item from the database and update the list
    private fun deleteItem(position: Int) {
        val totp = items[position]
        lifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            try {
                database.toTpDao().delete(totp)
                withContext(Dispatchers.Main) {
                    items.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, items.size)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Delete failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Inner ViewHolder class to hold item views
    inner class ViewHolder(itemView: View, val context: Context = itemView.context) : RecyclerView.ViewHolder(itemView) {
        private val secretTextView: TextView = itemView.findViewById(R.id.secret)
        private val issuerTextView: TextView = itemView.findViewById(R.id.issuer)
        private val labelTextView: TextView = itemView.findViewById(R.id.label)
        private val rowLayout: ConstraintLayout = itemView.findViewById(R.id.row_layout)

        // Bind the data to the views
        fun bind(totp: ToTp) {
            secretTextView.text = totpGenerator(totp.secret)
            issuerTextView.text = totp.IssuerName
            labelTextView.text = totp.label

            rowLayout.setOnLongClickListener {
                AlertDialog.Builder(context).apply {
                    setTitle("Delete Item")
                    setMessage("Are you sure you want to delete this item?")
                    setPositiveButton("Yes") { dialog, _ ->
                        deleteItem(adapterPosition)
                        dialog.dismiss()
                    }
                    setNegativeButton("No") { dialog, _ ->
                        dialog.dismiss()
                    }
                    create()
                    show()
                }
                return@setOnLongClickListener true
            }
        }

        private fun totpGenerator(secret: String): String {
            val newSecret = secret.toByteArray()
            val googleAuthenticator = GoogleAuthenticator(newSecret)
            val code = googleAuthenticator.generate()
            return code
        }
    }
}