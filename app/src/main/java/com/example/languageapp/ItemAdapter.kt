package com.example.languageapp

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemAdapter(private val itemAdapterListener: ItemAdapterListener): RecyclerView.Adapter<ItemAdapter.ViewHolder>(){
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
        private val textViewLevel: TextView = itemView.findViewById(R.id.recyclerViewTextView)
        private val imageViewLevel: ImageView = itemView.findViewById(R.id.recyclerViewImage)

        fun update(levels: Levels) {
            textViewLevel.text = levels.level.toString()
            levels.levelImage?.let { imageViewLevel.setImageResource(it) }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(view, itemAdapterListener)
    }

    override fun onBindViewHolder(holder: ItemAdapter.ViewHolder, position: Int) {
        val level = LevelsModel.spanishLevels[position]
        holder.update(level)
    }

    override fun getItemCount(): Int {
        return LevelsModel.spanishLevels.size
    }
}