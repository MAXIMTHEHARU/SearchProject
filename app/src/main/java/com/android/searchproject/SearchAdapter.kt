package com.android.searchproject

import android.content.ClipData
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.android.searchproject.databinding.SearchRecyclerviewBinding
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SearchAdapter(private val context: Context, private val mItems: MutableList<Document>, private val favoriteItems : MutableList<Document>): RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    interface SearchClick {
        fun onClick(view: View, position: Int)
    }
    var searchClick : SearchClick? = null



    inner class SearchViewHolder(private val binding: SearchRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.searchImage
        val name = binding.searchName
        val dateTime = binding.searchDatetime
        val favorite = binding.searchFavorite
        val fullFavorite = binding.searchFillFavorite
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(SearchRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.itemView.setOnClickListener{
            searchClick?.onClick(holder.fullFavorite, position)
        }

        Glide.with(context)
            .load(mItems[position].thumbnailUrl)
            .override(200, 200)
            .into(holder.image) //이미지 받아와서 searchImage에 넣는다!
        holder.name.text = mItems[position].displaySiteName

        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
        holder.dateTime.text = mItems[position].dateTime
        holder.fullFavorite.isVisible = (favoriteItems.find {it == mItems[position]} != null)


    }
}
