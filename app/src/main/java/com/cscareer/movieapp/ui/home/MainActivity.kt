package com.cscareer.movieapp.ui.home

import android.content.Context
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.DimenRes
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.viewpager2.widget.ViewPager2
import com.cscareer.movieapp.R
import com.cscareer.movieapp.databinding.ActivityMainBinding
import com.cscareer.movieapp.pagination.MovieLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.abs

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var slideAdapter: SliderAdapter
    private lateinit var popularMovieAdapter: MovieAdapter

    private val movieViewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            title = ""
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_baseline_dehaze_24)
        }

        slideAdapter = SliderAdapter(this, ArrayList())
        popularMovieAdapter = MovieAdapter(this)

        bindUI()
        initViewModel()
    }

    private fun initViewModel() {
        binding.progressBar.isVisible = true
        movieViewModel.apply {
            readyNowPlayingMovies()
            getNowPlayingMovies().observe(this@MainActivity, {
                if (it != null) {
                    slideAdapter.setData(it.subList(0, 5))
                }
            })

            movieViewModel.popularMovies.observe(this@MainActivity) {
                popularMovieAdapter.submitData(this@MainActivity.lifecycle, it)
            }
        }
        binding.progressBar.isVisible = false
    }

    private fun bindUI() {

        binding.apply {

            val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
            val currentItemHorizontalMarginPx =
                resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
            val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
            val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
                page.translationX = -pageTranslationX * position
                page.scaleY = 1 - (0.25f * abs(position))
                page.alpha = 0.5f + (1 - abs(position))
            }

            val itemDecoration = HorizontalMarginItemDecoration(
                this@MainActivity,
                R.dimen.viewpager_current_item_horizontal_margin
            )

            viewPager.apply {
                adapter = slideAdapter
                offscreenPageLimit = 1
                setPageTransformer(pageTransformer)
                addItemDecoration(itemDecoration)
            }

            popularRecyclerView.apply {
                setHasFixedSize(false)
                val defaultPaddingHalf =
                    resources.getDimensionPixelOffset(R.dimen.default_padding_half)
                addItemDecoration(object : ItemDecoration() {
                    override fun getItemOffsets(
                        outRect: Rect,
                        view: View,
                        parent: RecyclerView,
                        state: RecyclerView.State
                    ) {
                        outRect.left = defaultPaddingHalf
                        outRect.right = defaultPaddingHalf
                        outRect.top = defaultPaddingHalf
                        outRect.bottom = defaultPaddingHalf
                    }
                })
                adapter = popularMovieAdapter.withLoadStateHeaderAndFooter(
                    header = MovieLoadStateAdapter { popularMovieAdapter.retry() },
                    footer = MovieLoadStateAdapter { popularMovieAdapter.retry() }
                )
            }

            retryButton.setOnClickListener {
                popularMovieAdapter.retry()
                movieViewModel.apply {
                    readyNowPlayingMovies()
                    getNowPlayingMovies().observe(this@MainActivity, {
                        if (it != null) {
                            slideAdapter.setData(it.subList(0, 5))
                        }
                    })
                }
            }
        }

        popularMovieAdapter.addLoadStateListener { loadState ->
            Log.d("MainActivity", loadState.source.refresh.toString())
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                layout.isVisible = loadState.source.refresh is LoadState.NotLoading
                errorLayout.isVisible = loadState.source.refresh is LoadState.Error

                //empty view
                if (loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && popularMovieAdapter.itemCount < 1) {
                    layout.isVisible = false
                    errorLayout.isVisible = true
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    class HorizontalMarginItemDecoration(context: Context, @DimenRes horizontalMarginInDp: Int) :
        RecyclerView.ItemDecoration() {

        private val horizontalMarginInPx: Int =
            context.resources.getDimension(horizontalMarginInDp).toInt()

        override fun getItemOffsets(
            outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
        ) {
            outRect.right = horizontalMarginInPx
            outRect.left = horizontalMarginInPx
        }

    }
}