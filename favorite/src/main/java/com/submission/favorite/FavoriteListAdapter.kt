package com.submission.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.submission.core.domain.usecase.model.FavoriteMovie
import com.submission.core.utils.ComponentSetup
import kotlinx.android.synthetic.main.favorite_item.view.*

class FavoriteListAdapter : RecyclerView.Adapter<FavoriteListAdapter.FavoriteViewHoler>() {

    private val listData = ArrayList<FavoriteMovie>()
    var removeData: ((FavoriteMovie) -> Unit)? = null
    var onItemClicked: ((Int) -> Unit)? = null

    fun setData(newlistData: List<FavoriteMovie>?) {
        if (newlistData == null) return
        listData.clear()
        listData.addAll(newlistData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHoler =
        FavoriteViewHoler(
            LayoutInflater.from(parent.context).inflate(R.layout.favorite_item, parent, false)
        )

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
                        it, itemView.favorite_image
                    )
                }
                itemView.favorite_title.text = title
                itemView.favorite_date.text = ComponentSetup.dateFormat(release_date)
            }
        }

        init {
            itemView.btn_remove.setOnClickListener {
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