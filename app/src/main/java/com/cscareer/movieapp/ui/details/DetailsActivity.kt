package com.cscareer.movieapp.ui.details

import android.annotation.SuppressLint
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.cscareer.movieapp.R
import com.cscareer.movieapp.data.model.MovieDetails
import com.cscareer.movieapp.databinding.ActivityDetailsBinding
import com.cscareer.movieapp.network.MovieApi
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    private lateinit var simpleDateFormat: SimpleDateFormat

    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            title = ""
            it.setDisplayHomeAsUpEnabled(false)
        }

        val movieId = intent.getIntExtra("movieId", 0)

        simpleDateFormat = SimpleDateFormat("d MMM, yyyy")

        detailViewModel.apply {
            readyMovieDetails(movieId)
            getMovieDetails().observe(this@DetailsActivity, {
                if (it != null) {
                    bindWithUi(it)
                    showData()
                } else {
                    showError()
                }
            })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindWithUi(movieDetails: MovieDetails) {
        val calendar = Calendar.getInstance()
        calendar.set(
            movieDetails.releaseDate.split("-")[0].toInt(),
            movieDetails.releaseDate.split("-")[1].toInt() - 1,
            movieDetails.releaseDate.split("-")[2].toInt()
        )
        binding.apply {
            back.setOnClickListener { finish() }
            title.text = movieDetails.title
            descriptionTitle.text = movieDetails.tagline
            ratingText.text = movieDetails.rating.toString()
            ratingBar.rating = movieDetails.rating.toFloat() / 2
            dateTime.text =
                "${movieDetails.runtime / 60}h ${movieDetails.runtime - ((movieDetails.runtime / 60) * 60)}min | ${
                    simpleDateFormat.format(calendar.time)
                }"
            description.text = movieDetails.overview
            reviews.text = "Reviews: ${movieDetails.voteCount} (User)"

            val posterUrl: String = MovieApi.PHOTO_BASE_URL + movieDetails.posterPath
            Glide.with(this@DetailsActivity)
                .load(posterUrl)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(poster)

            val layoutManager = FlexboxLayoutManager(this@DetailsActivity)
            layoutManager.flexDirection = FlexDirection.ROW
            layoutManager.justifyContent = JustifyContent.FLEX_START
            genreRecyclerView.apply {
                this.layoutManager = layoutManager
                val defaultPaddingHalf =
                    resources.getDimensionPixelOffset(R.dimen.default_padding_fourth)
                addItemDecoration(object : RecyclerView.ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        outRect.right = defaultPaddingHalf
                        outRect.bottom = defaultPaddingHalf
                    }
                })
                adapter = GenreAdapter(this@DetailsActivity, movieDetails.genres)
            }
        }
    }

    private fun showData() {
        binding.apply {
            errorText.isVisible = false
            layout.isVisible = true
            progressBar.isVisible = false
        }
    }

    private fun showError() {
        binding.apply {
            errorText.isVisible = true
            layout.isVisible = false
            progressBar.isVisible = false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_details, menu)
        return true
    }
}