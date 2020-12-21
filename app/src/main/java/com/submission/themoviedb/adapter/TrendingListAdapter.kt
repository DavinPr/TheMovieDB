package com.submission.themoviedb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.submission.core.domain.usecase.model.TrendingMovie
import com.submission.core.utils.ComponentSetup
import com.submission.themoviedb.databinding.TrendingItemBinding

class TrendingListAdapter : RecyclerView.Adapter<TrendingListAdapter.FilmViewHoler>() {

    //View Binding
    private var _binding : TrendingItemBinding? = null
    private val binding get() = _binding!!

    private val listData = ArrayList<TrendingMovie>()
    var onClickItem : ((Int) -> Unit)? = null

    fun setData(newlistData: List<TrendingMovie>?) {
        if (newlistData == null) return
        listData.clear()
        listData.addAll(newlistData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmViewHoler {
        _binding = TrendingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilmViewHoler(binding.root)
    }

    override fun onBindViewHolder(holder: FilmViewHoler, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listData.size

    inner class FilmViewHoler(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: TrendingMovie) {
            with(movie) {
                ComponentSetup.loadImage(
                    itemView.context,
                    poster_path, binding.trendingImage
                )
                binding.trendingTitle.text = title
                binding.trendingDate.text = ComponentSetup.dateFormat(release_date)
            }
        }
        init {
            itemView.setOnClickListener {
                onClickItem?.invoke(listData[adapterPosition].id)
            }
        }
    }
}