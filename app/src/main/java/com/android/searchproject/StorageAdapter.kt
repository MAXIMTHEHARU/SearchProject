package com.android.searchproject

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.searchproject.databinding.SearchRecyclerviewBinding
import com.android.searchproject.databinding.StorageRecyclerviewBinding
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Locale

class StorageAdapter(private val context: Context, private val favoriteItems: MutableList<Document>) :
    RecyclerView.Adapter<StorageAdapter.StorageViewHolder>() {

    interface StorageImageClick {
        fun onClick(position: Int)
    }

    var storageImageClick: StorageImageClick? = null

    inner class StorageViewHolder(private val binding: SearchRecyclerviewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val thumbnail = binding.searchImage
        val site = binding.searchName
        val datetime = binding.searchDatetime
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StorageViewHolder {
        return StorageViewHolder(
            SearchRecyclerviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return favoriteItems.size
    }

    override fun onBindViewHolder(holder: StorageViewHolder, position: Int) {
        holder.thumbnail.setOnClickListener {
            storageImageClick?.onClick(position)
        }

        Glide.with(context)
            .load(favoriteItems[position].thumbnailUrl)
            .into(holder.thumbnail)
        holder.site.text = favoriteItems[position].displaySiteName
        holder.datetime.text = favoriteItems[position].dateTime
    }
}