package com.cscareer.movieapp.ui.details

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cscareer.movieapp.data.model.Genre
import com.cscareer.movieapp.databinding.GenreItemBinding

class GenreAdapter(private val context: Context, private val genreList: List<Genre>) :
    RecyclerView.Adapter<GenreAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GenreItemBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val genre = genreList[position]
        holder.bind(genre)
    }

    override fun getItemCount(): Int = genreList.size

    inner class ViewHolder(private val binding: GenreItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(genre: Genre) {
            binding.name.text = genre.name
        }
    }
}