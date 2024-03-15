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
import kr.co.helicopark.movienoti.ui.bottom.MovieBottomFragment
import kr.co.helicopark.movienoti.ui.datePicker
import kr.co.helicopark.movienoti.ui.getTheaterName
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class ReservationFragment : Fragment() {
    private lateinit var binding: FragmentReservationBinding
    private val viewModel: ReservationViewModel by viewModels()

    private val adapter: ReservationAdapter by lazy {
        ReservationAdapter { personalReservationMovie ->
            AlertDialog.Builder(requireContext()).apply {
                val formattedDate = SimpleDateFormat("yy년 MM월 dd일", Locale.getDefault()).format(personalReservationMovie.reservationDate)
                val movieFormat = if (personalReservationMovie.movieFormat == "IMAX") {
                    "IMAX "
                } else {
                    ""
                }

                setTitle(R.string.dialog_reservation_title)
                setMessage(
                    String.format(
                        getString(R.string.dialog_reservation_message_edit_format),
                        getTheaterName(personalReservationMovie.areaCode, personalReservationMovie.theaterCode),
                        movieFormat,
                        personalReservationMovie.movieTitle,
                        formattedDate
                    )
                )

                setPositiveButton(R.string.dialog_edit) { _, _ ->
                    datePicker(personalReservationMovie.movieTitle).apply {
                        addOnPositiveButtonClickListener { reservationDate ->
                            MovieBottomFragment { isSuccess ->
                                if (isSuccess) {
                                    viewModel.initReservationMovieList()
                                }
                            }.let { fragment ->
                                Bundle().let {
                                    it.putLong("date", personalReservationMovie.date)
                                    it.putString("movieTitle", personalReservationMovie.movieTitle)
                                    it.putLong("reservationDate", reservationDate)
                                    it.putString("thumb", personalReservationMovie.thumb)
                                    it.putBoolean("isUpdate", true)
                                    fragment.arguments = it
                                }

                                fragment.show(requireActivity().supportFragmentManager, "MovieBottomFragment")
                            }
                        }
                    }.show(requireActivity().supportFragmentManager, "datePicker")
                }

                setNegativeButton(R.string.dialog_close) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }

                setNeutralButton(R.string.dialog_delete) { _, _ ->
                    AlertDialog.Builder(requireContext()).apply {
                        setTitle(R.string.dialog_reservation_title)
                        setMessage(String.format(getString(R.string.dialog_reservation_delete_message), personalReservationMovie.movieTitle))
                        setPositiveButton(R.string.dialog_delete) { _, _ ->
                            viewModel.deleteReservationMovie(personalReservationMovie.date)
                        }
                        setNegativeButton(R.string.dialog_cancel) { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }
                        show()
                    }
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