package kr.co.helicopark.movienoti.presentation.cgv

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.helicopark.movienoti.databinding.ItemMovieListBinding
import kr.co.helicopark.movienoti.presentation.model.CgvMovieItem

class CgvAdapter(private val onItemClickListener: (CgvMovieItem) -> Unit) : ListAdapter<CgvMovieItem, CgvAdapter.ViewHolder>(MovieDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemMovieListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    inner class ViewHolder(private val binding: ItemMovieListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            Glide.with(binding.root.context).load(getItem(adapterPosition).thumb).centerInside().override(197, 260).into(binding.ivMovieItemThumb)
            binding.tvMovieItemTitle.text = Html.fromHtml(getItem(adapterPosition).title, Html.FROM_HTML_MODE_LEGACY)
            binding.tvMovieItemReservationRate.text = Html.fromHtml(getItem(adapterPosition).reservationRate, Html.FROM_HTML_MODE_LEGACY)
            binding.tvMovieItemReleaseDate.text = Html.fromHtml(getItem(adapterPosition).releaseDate, Html.FROM_HTML_MODE_LEGACY)

            binding.root.setOnClickListener {
                onItemClickListener.invoke(getItem(adapterPosition))
            }
        }
    }
}

object MovieDiffUtil : DiffUtil.ItemCallback<CgvMovieItem>() {
    override fun areItemsTheSame(oldItem: CgvMovieItem, newItem: CgvMovieItem): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: CgvMovieItem, newItem: CgvMovieItem): Boolean {
        return oldItem == newItem
    }
}