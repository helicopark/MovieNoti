package kr.co.helicopark.movienoti.ui.cgv.bottom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.helicopark.movienoti.databinding.ItemBottomTheaterBinding
import kr.co.helicopark.movienoti.ui.model.TheaterItem

class MovieBottomTheaterAdapter(private val onItemClickListener: (TheaterItem) -> Unit) :
    ListAdapter<TheaterItem, MovieBottomTheaterAdapter.ViewHolder>(TheaterDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemBottomTheaterBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    inner class ViewHolder(private val binding: ItemBottomTheaterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val theaterItem = getItem(adapterPosition)

            binding.clBottomTheater.setOnClickListener {
                onItemClickListener.invoke(theaterItem)
            }

            binding.tvBottomTheaterName.text = theaterItem.name
        }
    }
}

object TheaterDiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<TheaterItem>() {
    override fun areItemsTheSame(oldItem: TheaterItem, newItem: TheaterItem): Boolean {
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItem: TheaterItem, newItem: TheaterItem): Boolean {
        return oldItem == newItem
    }
}