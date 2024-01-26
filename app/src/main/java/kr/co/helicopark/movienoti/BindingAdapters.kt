package kr.co.helicopark.movienoti

import android.view.View
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.helicopark.movienoti.domain.model.Resource

@BindingAdapter("adapter", "submitList", requireAll = true)
fun bindRecyclerView(view: RecyclerView, adapter: RecyclerView.Adapter<*>, submitList: Resource<List<Any>>) {
    view.adapter = adapter
    view.itemAnimator = DefaultItemAnimator()
    view.layoutManager = LinearLayoutManager(view.context)

    if (submitList is Resource.Success) {
        (adapter as ListAdapter<Any, *>).submitList(submitList.data)
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