package com.astru43.youtube_checker.ui.main.management

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.astru43.youtube_checker.R
import com.google.api.services.youtube.model.Playlist

class SaveListAdapter(private val items: List<Playlist>) :
    RecyclerView.Adapter<SaveListAdapter.ViewHolder>() {

    private var selectedPosition = -1
    var selectedItem = -1
        private set

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.listItem)

        init {
            textView.setOnClickListener {
                Log.d("ListId", textView.text.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = items[position].snippet.title

        if (selectedPosition == position)
            holder.textView.setBackgroundColor(Color.DKGRAY)
        else
            holder.textView.setBackgroundColor(Color.TRANSPARENT)
        holder.textView.setOnClickListener {
            selectedPosition = position
            selectedItem = holder.adapterPosition
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = items.size

}