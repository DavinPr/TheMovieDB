package com.submission.themoviedb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.submission.core.domain.usecase.model.CastMovie
import com.submission.core.utils.ComponentSetup
import com.submission.themoviedb.R
import kotlinx.android.synthetic.main.cast_item.view.*

class CastListAdapter : RecyclerView.Adapter<CastListAdapter.CastViewHolder>() {

    private val listData = ArrayList<CastMovie>()

    fun setData(newListData: List<CastMovie>?) {
        if (newListData == null) return
        listData.clear()
        listData.addAll(newListData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CastViewHolder = CastViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.cast_item, parent, false)
    )


    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = if (listData.size > 5) 5 else listData.size


    class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(cast: CastMovie) {
            itemView.cast_character.text = cast.character
            itemView.cast_name.text = cast.name
            cast.profile_path?.let {
                ComponentSetup.loadImage(
                    itemView.context,
                    it, itemView.cast_image
                )
            }
        }
    }
}