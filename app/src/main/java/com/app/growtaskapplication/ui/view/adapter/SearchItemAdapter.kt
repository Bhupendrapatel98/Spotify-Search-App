package com.app.growtaskapplication.ui.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.growtaskapplication.R
import com.app.growtaskapplication.data.model.Item
import com.app.growtaskapplication.utills.OnItemClick
import com.bumptech.glide.Glide

class SearchItemAdapter(
    private val mList: List<Item>,
    private val viewType: String,
    private val onItemClick: (String,String)->Unit
) : RecyclerView.Adapter<SearchItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_albums, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val singleUnit = mList[position]

        if (viewType == "tracks") {
            if (singleUnit.album?.images?.isNotEmpty() == true) {
                Glide.with(holder.imageView.context).load(singleUnit.album.images[0].url).into(holder.imageView)
            }
        } else {
            if (singleUnit.images.isNotEmpty()) {
                Glide.with(holder.imageView.context).load(singleUnit.images[0].url).into(holder.imageView)
            }
        }

        holder.textView.text = singleUnit.name

        when (viewType) {
            "artist" -> {
                holder.artistName.text = "${singleUnit.followers?.total.toString()} followers"
                holder.releaseDate.text = "${singleUnit.popularity.toString()} Popularity"
            }
            "album" -> {
                holder.artistName.text = singleUnit.artists[0].name
                holder.releaseDate.text = singleUnit.release_date
            }
            "playlist" -> {
                holder.artistName.text = singleUnit.description
                holder.releaseDate.text = singleUnit.owner?.display_name
            }
            "tracks" -> {
                holder.artistName.text = singleUnit.artists[0].name
                holder.releaseDate.text = singleUnit.album?.release_date
            }
        }

        holder.itemView.setOnClickListener {
            onItemClick(viewType, singleUnit.id)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image)
        val textView: TextView = itemView.findViewById(R.id.name)
        val artistName: TextView = itemView.findViewById(R.id.artist_name)
        val releaseDate: TextView = itemView.findViewById(R.id.releaseDate)
    }
}