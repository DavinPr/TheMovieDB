package com.submission.favorite

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.fondesa.recyclerviewdivider.dividerBuilder
import com.submission.core.data.Resource
import com.submission.favorite.databinding.ActivityFavoriteBinding
import com.submission.themoviedb.detail.DetailActivity
import com.submission.themoviedb.helper.barSetup
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding : ActivityFavoriteBinding
    private val favoriteViewModel: FavoriteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        barSetup(window, applicationContext)

        loadKoinModules(favoriteModule)

        getFavoriteData()
    }

    private fun getFavoriteData() {
        val favoriteAdapter = FavoriteListAdapter()
        favoriteAdapter.onItemClicked = { id ->
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_DATA, id)
            startActivity(intent)
        }
        favoriteAdapter.removeData = { favorite ->
            favoriteViewModel.deleteFavoriteMovie(favorite)
        }
        favoriteViewModel.getAllFavorite.observe(this, { favorite ->
            if (favorite != null) {
                when (favorite) {
                    is Resource.Loading -> binding.favoriteProgressbar.visibility = View.VISIBLE
                    is Resource.Success -> {
                        binding.favoriteProgressbar.visibility = View.GONE
                        favoriteAdapter.setData(favorite.data)
                        favorite.data?.let {
                            if (it.isEmpty()) {
                                binding.tvEmpty.visibility = View.VISIBLE
                                binding.rvFavorite.visibility = View.GONE
                            } else {
                                binding.tvEmpty.visibility = View.GONE
                                binding.rvFavorite.visibility = View.VISIBLE
                            }
                        }
                    }
                    is Resource.Error -> {
                        binding.favoriteProgressbar.visibility = View.GONE
                    }
                }
            }
        })
        with(binding.rvFavorite) {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            hasFixedSize()
            adapter = favoriteAdapter
        }
        dividerBuilder()
            .tint(ContextCompat.getColor(applicationContext, R.color.divideColor))
            .build()
            .addTo(binding.rvFavorite)
    }
}