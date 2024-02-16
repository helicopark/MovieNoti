package kr.co.helicopark.movienoti.ui.cgv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CalendarConstraints.DateValidator
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kr.co.helicopark.movienoti.databinding.FragmentCgvBinding
import kr.co.helicopark.movienoti.ui.cgv.bottom.MovieBottomFragment
import java.util.Calendar


@AndroidEntryPoint
class CgvFragment : Fragment() {
    private lateinit var binding: FragmentCgvBinding
    private val viewModel: CgvViewModel by viewModels()

    private val adapter by lazy {
        CgvAdapter { movieListItem ->
            showDatePicker(movieListItem.title) { reservationDate ->
                MovieBottomFragment().let { fragment ->
                    Bundle().let {
                        it.putString("movieTitle", movieListItem.title)
                        it.putLong("reservationDate", reservationDate)
                        it.putString("thumb", movieListItem.thumb)
                        fragment.arguments = it
                    }

                    fragment.show(requireActivity().supportFragmentManager, "MovieBottomFragment")
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCgvBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.adapter = adapter
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvListReservationRateOrder.setOnClickListener {
            viewModel.initCgvMovieList(CgvOrder.ReservationRateOrder)
            binding.search.setQuery("", false)
        }

        binding.tvListAbcOrder.setOnClickListener {
            viewModel.initCgvMovieList(CgvOrder.AbcOrder)
            binding.search.setQuery("", false)
        }

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return true
            }
        })

        lifecycleScope.launch {
            viewModel.initCgvMovieList(CgvOrder.ReservationRateOrder)
        }
    }

    private fun showDatePicker(movieTitle: String, onPositiveButtonClickListener: MaterialPickerOnPositiveButtonClickListener<in Long>) {
        val datePickerBuilder = MaterialDatePicker.Builder.datePicker()
        datePickerBuilder.setTitleText(movieTitle)
        datePickerBuilder.setCalendarConstraints(calendarConstraints())

        val datePicker = datePickerBuilder.build()
        datePicker.addOnPositiveButtonClickListener(onPositiveButtonClickListener)
        datePicker.show(requireActivity().supportFragmentManager, datePicker.toString())
    }

    private fun calendarConstraints(): CalendarConstraints {
        val constraintsBuilderRange = CalendarConstraints.Builder()
        val calendarStart: Calendar = Calendar.getInstance()
        val calendarEnd: Calendar = Calendar.getInstance()
        calendarStart.add(Calendar.DATE, -1)
        calendarEnd.add(Calendar.MONTH, 1)

        constraintsBuilderRange.setStart(calendarStart.timeInMillis)
        constraintsBuilderRange.setEnd(calendarEnd.timeInMillis)

        val listValidators = ArrayList<DateValidator>()
        listValidators.add(DateValidatorPointForward.from(calendarStart.timeInMillis))
        listValidators.add(DateValidatorPointBackward.before(calendarEnd.timeInMillis))
        val validators = CompositeDateValidator.allOf(listValidators)

        constraintsBuilderRange.setValidator(validators)

        return constraintsBuilderRange.build()
    }
}