package com.galaxytechno.chat.movie

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.galaxytechno.chat.R
import com.galaxytechno.chat.common.Endpoint
import com.galaxytechno.chat.databinding.ItemListBinding

class MoviePagingDataAdapter(
    private val isDark: Boolean,
    private val onClick: (Int) -> Unit
) : PagingDataAdapter<Movie, RecyclerView.ViewHolder>(MovieComparator) {

    fun getClickItem(position: Int): Movie? = getItem(position)

    object MovieComparator : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie) =
            oldItem.title == newItem.title

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
    }

    inner class MovieViewHolder(private val binding: ItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val placeholder =
            if (isDark) R.drawable.placeholder_dark else R.drawable.place_holder

        fun bind(item: Movie?) = with(binding) {

            item?.let {
                this.tvTitle.text = item.title
//                this.tvOrderCount.text = item.vote_average.toString()
//                this.tvTotalPrice.text = item.vote_count.toString()
                Glide.with(itemView.context)
                    .load(Endpoint.IMAGE_URL + item.backdrop_path)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .placeholder(placeholder)
                    .into(this.img)
            }
            itemView.setOnClickListener { onClick(absoluteAdapterPosition) }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as MovieViewHolder).bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }
}