package com.example.languageapp

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(private val cursor: Cursor, private val itemAdapterListener: ItemAdapterListener): RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    interface ItemAdapterListener{
        fun click(position: Int)
    }
    class ViewHolder(itemView: View, private val itemAdapterListener: ItemAdapterListener): RecyclerView.ViewHolder(itemView){
        init {
            itemView.setOnClickListener{
                val position = adapterPosition
                itemAdapterListener.click(position)
            }
        }
        // init our web view
        // init our 2 text views
//        private val webView: WebView = itemView.findViewById(R.id.comicImage)
//        private val textViewNumber: TextView = itemView.findViewById(R.id.commicNumber)
//        private val textViewTitle: TextView = itemView.findViewById(R.id.comicTitle)

        fun update(cursor: Cursor) {
//            val recyclerViewImg = cursor.getColumnIndex("webImg")
//            val recyclerViewNumber = cursor.getColumnIndex("comicNumber")
//            val recyclerViewTitle = cursor.getColumnIndex("comicTitle")
//
//            webView.loadUrl(cursor.getString(recyclerViewImg))
//            textViewNumber.text = cursor.getInt(recyclerViewNumber).toString()
//            textViewTitle.text = cursor.getString(recyclerViewTitle)

        }


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view, itemAdapterListener)
    }

    override fun onBindViewHolder(holder: ItemAdapter.ViewHolder, position: Int) {
        cursor.moveToPosition(position)
        holder.update(cursor)
    }

    override fun getItemCount(): Int {
        return cursor.count
    }
}