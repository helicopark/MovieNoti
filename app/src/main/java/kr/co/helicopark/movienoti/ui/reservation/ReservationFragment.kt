package kr.co.helicopark.movienoti.ui.reservation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kr.co.helicopark.movienoti.R
import kr.co.helicopark.movienoti.databinding.FragmentReservationBinding
import kr.co.helicopark.movienoti.ui.getTheaterName
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class ReservationFragment : Fragment() {
    private lateinit var binding: FragmentReservationBinding
    private val viewModel: ReservationViewModel by viewModels()

    private val adapter: ReservationAdapter by lazy {
        ReservationAdapter {
            AlertDialog.Builder(requireContext()).apply {
                val formattedDate = SimpleDateFormat("yy년 MM월 dd일", Locale.getDefault()).format(it.reservationDate)

                setTitle(R.string.dialog_reservation_title)
                setMessage(
                    String.format(
                        getString(R.string.dialog_reservation_message_edit_format),
                        getTheaterName(it.areaCode, it.theaterCode),
                        it.movieName,
                        it.movieFormat,
                        formattedDate
                    )
                )
                setPositiveButton(R.string.dialog_edit) { _, _ ->

                }

                setNegativeButton(R.string.dialog_cancel) { dialogInteface, _ ->
                    dialogInteface.dismiss()
                }

                setNeutralButton(R.string.dialog_delete) { _, _ ->
                    viewModel.deleteReservationMovie(it.date)
                }
            }.show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentReservationBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.adapter = adapter
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.initReservationMovieList()
        }
    }
}