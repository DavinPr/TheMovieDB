package com.submission.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.submission.core.domain.usecase.model.FavoriteMovie
import com.submission.core.utils.ComponentSetup
import com.submission.favorite.databinding.FavoriteItemBinding

class FavoriteListAdapter : RecyclerView.Adapter<FavoriteListAdapter.FavoriteViewHoler>() {

    private var _binding : FavoriteItemBinding? = null
    private val binding get() = _binding!!

    private val listData = ArrayList<FavoriteMovie>()
    var removeData: ((FavoriteMovie) -> Unit)? = null
    var onItemClicked: ((Int) -> Unit)? = null

    fun setData(newlistData: List<FavoriteMovie>?) {
        if (newlistData == null) return
        listData.clear()
        listData.addAll(newlistData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHoler {
        _binding = FavoriteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteViewHoler(binding.root)
    }

    override fun onBindViewHolder(holder: FavoriteViewHoler, position: Int) {
        val data = listData[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = listData.size

    inner class FavoriteViewHoler(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: FavoriteMovie) {
            with(movie) {
                poster_path?.let {
                    ComponentSetup.loadImage(
                        itemView.context,
                        it, binding.favoriteImage
                    )
                }
                binding.favoriteTitle.text = title
                binding.favoriteDate.text = ComponentSetup.dateFormat(release_date, binding.root.context)
            }
        }

        init {
            binding.btnRemove.setOnClickListener {
                removeData?.invoke(listData[adapterPosition])
                ComponentSetup.setSnackbar(
                    itemView.context.getString(R.string.remove_favorite_message),
                    itemView
                )
            }
            itemView.setOnClickListener {
                onItemClicked?.invoke(listData[adapterPosition].id)
            }
        }
    }
}