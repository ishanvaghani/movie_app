package com.cscareer.movieapp.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.cscareer.movieapp.R
import com.cscareer.movieapp.data.model.Movie
import com.cscareer.movieapp.databinding.SliderItemBinding
import com.cscareer.movieapp.network.MovieApi
import com.cscareer.movieapp.ui.details.DetailsActivity

class SliderAdapter(
    private val context: Context,
    private var sliders: List<Movie>
) : RecyclerView.Adapter<SliderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = SliderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val slider = sliders[position]

        holder.bind(slider)
    }

    override fun getItemCount(): Int = sliders.size

    inner class ViewHolder(private val binding: SliderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            val posterUrl: String = MovieApi.PHOTO_BASE_URL + movie.posterPath

            Glide.with(context)
                .load(posterUrl)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(binding.image)

            binding.root.setOnClickListener {
                context.startActivity(Intent(context, DetailsActivity::class.java).also {
                    it.putExtra("movieId", movie.id)
                })
            }
        }
    }

    fun setData(sliders: List<Movie>) {
        this.sliders = sliders
        notifyDataSetChanged()
    }
}