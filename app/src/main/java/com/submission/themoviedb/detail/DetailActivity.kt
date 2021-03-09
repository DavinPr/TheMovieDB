package com.submission.themoviedb.detail

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.submission.core.data.Resource
import com.submission.core.domain.usecase.model.DetailMovie
import com.submission.core.utils.ComponentSetup
import com.submission.core.utils.DataMapper
import com.submission.themoviedb.R
import com.submission.themoviedb.databinding.ActivityDetailBinding
import com.submission.themoviedb.detail.attribute.DetailAttributeFragment
import com.submission.themoviedb.detail.cast.CastDetailFragment
import com.submission.themoviedb.helper.blurredImage
import org.koin.android.viewmodel.ext.android.viewModel

class DetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_DATA = "extra_data"
    }

    private val detailViewModel: DetailViewModel by viewModel()
    private lateinit var binding: ActivityDetailBinding
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(
            findViewById(R.id.detail_container)
        ) { _, insets ->
            val margin = insets.systemWindowInsetTop + 16
            val menuLayoutParams =
                binding.detailBtnBack.layoutParams as ViewGroup.MarginLayoutParams
            menuLayoutParams.setMargins(0, margin, 0, 0)
            binding.detailBtnBack.layoutParams = menuLayoutParams
            insets.consumeSystemWindowInsets()
        }


        val id = intent.getIntExtra(EXTRA_DATA, 0)
        detailViewModel.stateFavorite(id).observe(this) { state ->
            setFavorite(state)
        }

        detailViewModel.detailData(id).observe(this) { detail ->
            when (detail) {
                is Resource.Loading -> {
                    binding.detailContainer.root.visibility = View.GONE
                    binding.detailBtnFavorite.visibility = View.GONE
                    binding.detailProgressBar.root.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.detailContainer.root.visibility = View.VISIBLE
                    binding.detailBtnFavorite.visibility = View.VISIBLE
                    binding.detailProgressBar.root.visibility = View.GONE
                    showDetailMovie(detail.data)
                    detail.data?.let { setDataBundle(it) }
                        ?.let { sectionsPagerAdapter.setAttributeBundle(it) }
                    detail.data?.let { setCastBundle(it) }
                        ?.let { sectionsPagerAdapter.setCastBundle(it) }

                    val favoriteData = detail.data?.let {
                        DataMapper.mapDetailMovieToFavoriteMovie(
                            it
                        )
                    }
                    binding.detailBtnFavorite.setOnCheckedChangeListener { _, isChecked ->
                        if (isChecked) {
                            ComponentSetup.setSnackbar(
                                getString(R.string.add_favorite_message),
                                binding.detailContainer.dataActivityDetail.detailTabLayout
                            )
                            favoriteData?.let { detailViewModel.insertFavorite(it) }
                        } else {
                            ComponentSetup.setSnackbar(
                                getString(R.string.remove_favorite_message),
                                binding.detailContainer.dataActivityDetail.detailTabLayout
                            )
                            favoriteData?.let { detailViewModel.deleteFavorite(it) }
                        }
                    }
                    setTabLayout()

                }
                is Resource.Error -> {
                    binding.detailContainer.root.visibility = View.GONE
                    binding.detailBtnFavorite.visibility = View.GONE
                    binding.detailProgressBar.root.visibility = View.GONE
                    binding.viewError.root.visibility = View.VISIBLE
                }
            }
        }
        binding.detailBtnBack.setOnClickListener {
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
    }

    private fun setFavorite(state: Boolean) {
        binding.detailBtnFavorite.isChecked = state
    }

    private fun showDetailMovie(detailMovie: DetailMovie?) {
        detailMovie?.let { detail ->
            binding.detailContainer.detailTopContainer.detailTitle.text = detail.title
            binding.detailContainer.detailTopContainer.detailDate.text =
                ComponentSetup.dateFormat(detail.release_date, binding.root.context)
            binding.detailContainer.detailTopContainer.detailRuntime.text =
                ComponentSetup.runtimeFormat(detail.runtime)
            val listGenre = ArrayList<String>()
            detail.genreMovie?.map {
                listGenre.add(it.name)
            }
            binding.detailContainer.detailTopContainer.detailGenre.text = listGenre.toString()
            detail.poster_path?.let {
                ComponentSetup.loadImage(
                    this,
                    it,
                    binding.detailContainer.detailPoster
                )
            }
            detail.backdrop_path?.let {
                blurredImage(
                    this,
                    it,
                    binding.detailContainer.detailBackdrop
                )
            }
        }
    }

    private fun setTabLayout() {
        val viewPager: ViewPager = binding.detailContainer.dataActivityDetail.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabLayout: TabLayout = binding.detailContainer.dataActivityDetail.detailTabLayout
        tabLayout.setupWithViewPager(viewPager)
    }

    private fun setDataBundle(detailMovie: DetailMovie): Bundle {
        return Bundle().apply {
            putString(DetailAttributeFragment.EXTRA_OVERVIEW, detailMovie.overview)
            putDouble(DetailAttributeFragment.EXTRA_RATING, detailMovie.vote_average)
            putDouble(DetailAttributeFragment.EXTRA_POPULARITY, detailMovie.popularity)
        }
    }

    private fun setCastBundle(detailMovie: DetailMovie): Bundle {
        return Bundle().apply {
            putInt(CastDetailFragment.EXTRA_ID, detailMovie.id)
        }
    }
}