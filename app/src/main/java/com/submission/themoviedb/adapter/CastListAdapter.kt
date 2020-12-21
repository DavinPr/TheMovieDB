package com.submission.themoviedb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.submission.core.domain.usecase.model.CastMovie
import com.submission.core.utils.ComponentSetup
import com.submission.themoviedb.databinding.CastItemBinding

class CastListAdapter : RecyclerView.Adapter<CastListAdapter.CastViewHolder>() {

    private var _binding : CastItemBinding? = null
    private val binding get() = _binding!!

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
    ): CastViewHolder {
        _binding = CastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CastViewHolder(binding.root)
    }


    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = if (listData.size > 5) 5 else listData.size


    inner class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(cast: CastMovie) {
            binding.castCharacter.text = cast.character
            binding.castName.text = cast.name
            cast.profile_path?.let {
                ComponentSetup.loadImage(
                    itemView.context,
                    it, binding.castImage
                )
            }
        }
    }
}