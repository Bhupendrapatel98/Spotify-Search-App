package com.app.growtaskapplication.ui.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.growtaskapplication.data.model.Item
import com.app.growtaskapplication.databinding.RvAlbumsBinding
import com.app.growtaskapplication.utills.UserType
import com.bumptech.glide.Glide

class SearchItemAdapter(
    private val mList: List<Item>,
    private val viewType: UserType,
    private val onItemClick: (UserType,String)->Unit
) : RecyclerView.Adapter<SearchItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding :RvAlbumsBinding = RvAlbumsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val singleUnit = mList[position]

        if (viewType == UserType.TRACKS) {
            if (singleUnit.album?.images?.isNotEmpty() == true) {
                Glide.with(holder.binding.image.context).load(singleUnit.album.images[0].url).into(holder.binding.image)
            }
        } else {
            if (singleUnit.images.isNotEmpty()) {
                Glide.with(holder.binding.image.context).load(singleUnit.images[0].url).into(holder.binding.image)
            }
        }

        holder.binding.name.text = singleUnit.name

        when (viewType) {
            UserType.ARTIST -> {
                holder.binding.artistName.text = "${singleUnit.followers?.total.toString()} followers"
                holder.binding.releaseDate.text = "${singleUnit.popularity.toString()} Popularity"
            }
            UserType.ALBUM -> {
                holder.binding.artistName.text = singleUnit.artists[0].name
                holder.binding.releaseDate.text = singleUnit.release_date
            }
            UserType.PLAYLIST -> {
                holder.binding.artistName.text = singleUnit.description
                holder.binding.releaseDate.text = singleUnit.owner?.display_name
            }
            UserType.TRACKS -> {
                holder.binding.artistName.text = singleUnit.artists[0].name
                holder.binding.releaseDate.text = singleUnit.album?.release_date
            }
        }

        holder.itemView.setOnClickListener {
            onItemClick(viewType, singleUnit.id)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(val binding: RvAlbumsBinding) : RecyclerView.ViewHolder(binding.root)
}