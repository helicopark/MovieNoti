package kr.co.helicopark.movienoti

import android.graphics.Typeface
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.helicopark.movienoti.domain.model.PersonalReservationMovie
import kr.co.helicopark.movienoti.domain.model.Resource
import kr.co.helicopark.movienoti.ui.cgv.CgvAdapter
import kr.co.helicopark.movienoti.ui.model.CgvMovieItem
import kr.co.helicopark.movienoti.ui.reservation.ReservationAdapter

@BindingAdapter("adapter", "submitList", requireAll = true)
fun bindCgvRecyclerView(view: RecyclerView, adapter: CgvAdapter, submitList: Resource<List<CgvMovieItem>?>) {
    view.adapter = adapter
    view.itemAnimator = DefaultItemAnimator()
    view.layoutManager = LinearLayoutManager(view.context)

    if (submitList is Resource.Success) {
        adapter.submitList(submitList.data) {
            view.scrollToPosition(0)
        }

        if (!submitList.data.isNullOrEmpty()) {
            adapter.list = submitList.data
        }
    }
}

@BindingAdapter("adapter", "submitList", requireAll = true)
fun bindReservationRecyclerView(view: RecyclerView, adapter: ReservationAdapter, submitList: Resource<List<PersonalReservationMovie>?>) {
    view.adapter = adapter
    view.itemAnimator = DefaultItemAnimator()
    view.layoutManager = LinearLayoutManager(view.context)

    if (submitList is Resource.Success) {
        adapter.submitList(submitList.data)
    }
}

@BindingAdapter("resource")
fun bindProgress(view: ProgressBar, resource: Resource<List<Any>>) {
    when (resource) {
        is Resource.Loading -> view.visibility = View.VISIBLE
        is Resource.Success -> view.visibility = View.GONE
        is Resource.Error -> view.visibility = View.GONE
    }
}

@BindingAdapter("isBold")
fun setBold(view: TextView, isBold: Boolean) {
    if (isBold) {
        view.setTypeface(null, Typeface.BOLD)
    } else {
        view.setTypeface(null, Typeface.NORMAL)
    }
}