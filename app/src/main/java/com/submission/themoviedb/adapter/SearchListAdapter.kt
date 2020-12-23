package com.submission.themoviedb.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.submission.core.domain.usecase.model.SearchMovie
import com.submission.core.utils.ComponentSetup
import com.submission.themoviedb.databinding.SearchItemBinding
import com.submission.themoviedb.detail.DetailActivity

class SearchListAdapter : RecyclerView.Adapter<SearchListAdapter.SearchViewHoler>() {

    private val listData = ArrayList<SearchMovie>()

    fun setData(newlistData: List<SearchMovie>?) {
        if (newlistData == null) return
        listData.clear()
        listData.addAll(newlistData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHoler {
        val binding = SearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchViewHoler(binding)
    }

    override fun onBindViewHolder(holder: SearchViewHoler, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listData.size

    inner class SearchViewHoler(private val binding: SearchItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: SearchMovie) {
            with(movie) {
                poster_path?.let {
                    ComponentSetup.loadImage(
                        itemView.context,
                        it, binding.searchImage
                    )
                }
                binding.searchTitle.text = title
                binding.searchDate.text = ComponentSetup.dateFormat(release_date)
            }
        }

        init {
            itemView.setOnClickListener {
                val intent = Intent(it.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_DATA, listData[adapterPosition].id)
                it.context.startActivity(intent)
            }
        }
    }
}