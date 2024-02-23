package kr.co.helicopark.movienoti

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.graphics.Typeface
import android.net.Uri
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.helicopark.movienoti.domain.model.PersonalReservationMovie
import kr.co.helicopark.movienoti.domain.model.RemoteConfigVersion
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

@BindingAdapter("version")
fun initVersion(view: TextView, remoteConfigVersion: Resource<RemoteConfigVersion>) {
    if (remoteConfigVersion is Resource.Success) {
        val versionName = remoteConfigVersion.data?.versionName ?: BuildConfig.VERSION_NAME
        view.text = String.format(view.context.getString(R.string.setting_version_content_format), BuildConfig.VERSION_NAME, versionName)
    } else {
        view.text = String.format(view.context.getString(R.string.setting_version_content_format), BuildConfig.VERSION_NAME, BuildConfig.VERSION_NAME)
    }
}

@BindingAdapter("update")
fun initUpdate(view: TextView, remoteConfigVersion: Resource<RemoteConfigVersion>) {
    if (remoteConfigVersion is Resource.Success) {
        val versionCode = remoteConfigVersion.data?.versionCode?.toInt() ?: BuildConfig.VERSION_CODE

        if (BuildConfig.VERSION_CODE >= versionCode) {
            view.text = view.context.getString(R.string.setting_version_latest)
            view.setTextColor(ContextCompat.getColor(view.context, R.color.blue))
            view.setOnClickListener(null)
        } else {
            view.text = view.context.getString(R.string.setting_version_update)
            view.setTextColor(ContextCompat.getColor(view.context, R.color.red))
            view.setOnClickListener {
                view.context.startActivity(Intent(ACTION_VIEW, Uri.parse("market://details?id=kr.co.helicopark.movienoti")))
            }
        }
    } else {
        view.text = view.context.getString(R.string.setting_version_latest)
        view.setTextColor(ContextCompat.getColor(view.context, R.color.blue))
        view.setOnClickListener(null)
    }
}

@BindingAdapter("visible")
fun showEmptyTextView(view: TextView, resource: Resource<List<Any>>) {
    when (resource) {
        is Resource.Loading -> view.visibility = View.GONE
        is Resource.Success -> {
            if ((resource.data?.size ?: 0) > 0) {
                view.visibility = View.GONE
            } else {
                view.visibility = View.VISIBLE
            }
        }

        is Resource.Error -> view.visibility = View.VISIBLE
    }
}