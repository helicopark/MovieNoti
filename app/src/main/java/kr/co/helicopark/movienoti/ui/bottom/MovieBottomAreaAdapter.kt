package kr.co.helicopark.movienoti.ui.bottom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.helicopark.movienoti.R
import kr.co.helicopark.movienoti.databinding.ItemBottomAreaBinding
import kr.co.helicopark.movienoti.ui.model.AreaItem

class MovieBottomAreaAdapter(private val onItemClickListener: (String) -> Unit) :
    ListAdapter<AreaItem, MovieBottomAreaAdapter.ViewHolder>(AreaDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemBottomAreaBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    inner class ViewHolder(private val binding: ItemBottomAreaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val areaItem = getItem(adapterPosition)

            if (areaItem.isSelected) {
                binding.clBottomArea.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.background))
                binding.tvBottomAreaName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.text_color))
            } else {
                binding.clBottomArea.setBackgroundResource(R.drawable.bottom_area_background_off)
                binding.tvBottomAreaName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.text_sub_color))
            }

            binding.tvBottomAreaName.text = areaItem.name

            binding.clBottomArea.setOnClickListener {
                onItemClickListener.invoke(areaItem.code)
            }
        }
    }
}

object AreaDiffUtil : DiffUtil.ItemCallback<AreaItem>() {
    override fun areItemsTheSame(oldItem: AreaItem, newItem: AreaItem): Boolean {
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItem: AreaItem, newItem: AreaItem): Boolean {
        return oldItem == newItem
    }
}