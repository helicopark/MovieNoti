package kr.co.helicopark.movienoti.presentation.reservation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.helicopark.movienoti.databinding.ItemReservationListBinding
import kr.co.helicopark.movienoti.domain.model.PersonalReservationMovie
import kr.co.helicopark.movienoti.presentation.getTheaterName
import java.text.SimpleDateFormat
import java.util.*

class ReservationAdapter : ListAdapter<PersonalReservationMovie, ReservationAdapter.ViewHolder>(MovieDiffUtil) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemReservationListBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    inner class ViewHolder(private val binding: ItemReservationListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            binding.tvReservationListItemTitle.text = getItem(adapterPosition).movieName
            binding.tvReservationListItemDate.text = SimpleDateFormat("yy.MM.dd", Locale.getDefault()).format(Date(getItem(adapterPosition).reservationDate))
            binding.tvReservationListItemTheater.text = getTheaterName(getItem(adapterPosition).areaCode, getItem(adapterPosition).theaterCode)
            binding.tvReservationListItemFormat.text = getItem(adapterPosition).movieFormat

            Glide.with(binding.root.context).load(getItem(adapterPosition).thumb).centerInside().override(197, 260).into(binding.ivReservationItemThumb)
        }
    }
}

object MovieDiffUtil : DiffUtil.ItemCallback<PersonalReservationMovie>() {
    override fun areItemsTheSame(oldItem: PersonalReservationMovie, newItem: PersonalReservationMovie): Boolean {
        return oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: PersonalReservationMovie, newItem: PersonalReservationMovie): Boolean {
        return oldItem == newItem
    }
}