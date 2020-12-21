package com.submission.favorite

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.fondesa.recyclerviewdivider.dividerBuilder
import com.submission.core.data.Resource
import com.submission.themoviedb.detail.DetailActivity
import com.submission.themoviedb.helper.barSetup
import kotlinx.android.synthetic.main.activity_favorite.*
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteActivity : AppCompatActivity() {

    private val favoriteViewModel: FavoriteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

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
                    is Resource.Loading -> favorite_progressbar.visibility = View.VISIBLE
                    is Resource.Success -> {
                        favorite_progressbar.visibility = View.GONE
                        favoriteAdapter.setData(favorite.data)
                        favorite.data?.let {
                            if (it.isEmpty()) {
                                tv_empty.visibility = View.VISIBLE
                                rv_favorite.visibility = View.GONE
                            } else {
                                tv_empty.visibility = View.GONE
                                rv_favorite.visibility = View.VISIBLE
                            }
                        }
                    }
                    is Resource.Error -> {
                        favorite_progressbar.visibility = View.GONE
                    }
                }
            }
        })
        with(rv_favorite) {
            layoutManager = LinearLayoutManager(this@FavoriteActivity)
            hasFixedSize()
            adapter = favoriteAdapter
        }
        dividerBuilder()
            .tint(ContextCompat.getColor(applicationContext, R.color.divideColor))
            .build()
            .addTo(rv_favorite)
    }
}