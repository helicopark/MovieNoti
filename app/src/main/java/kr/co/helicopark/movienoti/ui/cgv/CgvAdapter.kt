package kr.co.helicopark.movienoti.ui.cgv

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.helicopark.movienoti.databinding.ItemMovieListBinding
import kr.co.helicopark.movienoti.ui.model.CgvMovieItem

class CgvAdapter(private val onItemClickListener: (CgvMovieItem) -> Unit) : ListAdapter<CgvMovieItem, CgvAdapter.ViewHolder>(MovieDiffUtil), Filterable {
    var list = listOf<CgvMovieItem>()

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

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filteredList = if (constraint.isNullOrEmpty()) {
                    list
                } else {
                    list.filter {
                        it.title.contains(constraint)
                    }.map {
                        it
                    }
                }

                val results = FilterResults()
                results.values = filteredList
                return results
            }

            override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
                try {
                    submitList(filterResults?.values as List<CgvMovieItem>)
                } catch (classCastException: ClassCastException) {
                    submitList(list)
                }
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