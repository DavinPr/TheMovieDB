package com.submission.themoviedb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.submission.core.domain.usecase.model.DiscoverMovie
import com.submission.core.utils.ComponentSetup
import com.submission.themoviedb.R
import kotlinx.android.synthetic.main.discover_item.view.*

class DiscoverListAdapter :
    RecyclerView.Adapter<DiscoverListAdapter.DiscoverViewHolder>() {
    private var listData = ArrayList<DiscoverMovie>()
    var onClickItem: ((Int) -> Unit)? = null

    fun setData(newListData: List<DiscoverMovie>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiscoverViewHolder =
        DiscoverViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.discover_item, parent, false)
        )

    override fun onBindViewHolder(holder: DiscoverViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listData.size

    inner class DiscoverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: DiscoverMovie) {
            with(movie) {
                itemView.discover_title.text = title
                itemView.discover_rating_value.text = vote_average.toString()
                itemView.discover_ratingbar.apply {
                    stepSize = 0.1f
                    rating = vote_average.toFloat()
                }
                ComponentSetup.loadImage(
                    itemView.context,
                    backdrop_path, itemView.discover_image
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