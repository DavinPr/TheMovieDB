package com.submission.themoviedb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.submission.core.domain.usecase.model.DiscoverMovie
import com.submission.core.utils.ComponentSetup
import com.submission.themoviedb.databinding.DiscoverItemBinding

class DiscoverListAdapter :
    RecyclerView.Adapter<DiscoverListAdapter.DiscoverViewHolder>() {

    private var _binding : DiscoverItemBinding? = null
    private val binding get() = _binding!!

    private var listData = ArrayList<DiscoverMovie>()
    var onClickItem: ((Int) -> Unit)? = null

    fun setData(newListData: List<DiscoverMovie>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoverViewHolder {
        _binding = DiscoverItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiscoverViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: DiscoverViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listData.size

    inner class DiscoverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: DiscoverMovie) {
            with(movie) {
                binding.discoverTitle.text = title
                binding.discoverRatingValue.text = vote_average.toString()
                binding.discoverRatingbar.apply {
                    stepSize = 0.1f
                    rating = vote_average.toFloat()
                }
                ComponentSetup.loadImage(
                    itemView.context,
                    backdrop_path, binding.discoverImage
                )
            }
        }

        init {
            itemView.setOnClickListener {
                onClickItem?.invoke(listData[adapterPosition].id)
            }
        }
    }
}