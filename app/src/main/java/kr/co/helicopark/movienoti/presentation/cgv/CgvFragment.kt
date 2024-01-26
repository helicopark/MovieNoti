package kr.co.helicopark.movienoti.presentation.cgv

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
import kr.co.helicopark.movienoti.R
import kr.co.helicopark.movienoti.databinding.FragmentCgvBinding
import kr.co.helicopark.movienoti.presentation.cgv.bottom.MovieBottomFragment
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
            viewModel.initCgvMovieList("", 0)
        }

        binding.tvListAbcOrder.setOnClickListener {
            viewModel.initCgvMovieList("", 1)
        }

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.initCgvMovieList(newText ?: "", 2)
                return true
            }

        })

        lifecycleScope.launch {
            viewModel.initCgvMovieList("", 0)
        }
    }

    private fun showDatePicker(movieTitle: String, onPositiveButtonClickListener: MaterialPickerOnPositiveButtonClickListener<in Long>) {
        val datePickerBuilder = MaterialDatePicker.Builder.datePicker()
        datePickerBuilder.setTitleText(String.format(getString(R.string.date_picker_format_title), movieTitle))
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