package kr.co.helicopark.movienoti.ui.cgv

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnCloseListener
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
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
import kr.co.helicopark.movienoti.ui.cgv.bottom.MovieBottomFragment
import java.util.Calendar


@AndroidEntryPoint
class CgvFragment : Fragment() {
    private lateinit var binding: FragmentCgvBinding
    private val viewModel: CgvViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (!isGranted) {
            androidx.appcompat.app.AlertDialog.Builder(requireContext()).apply {
                setCancelable(false)
                setTitle(R.string.dialog_default_title)
                setMessage(R.string.dialog_reject_post_notification_message)
                setPositiveButton(R.string.dialog_setting) { dialogInterface, _ ->
                    startSettingAppForNotificationPermission()
                    dialogInterface.dismiss()
                }
                setNegativeButton(R.string.dialog_cancel) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
            }.show()
        } else {

        }
    }

    private val adapter by lazy {
        CgvAdapter { movieListItem ->
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
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
            } else {
                androidx.appcompat.app.AlertDialog.Builder(requireContext()).apply {
                    setCancelable(false)
                    setTitle(R.string.dialog_default_title)
                    setMessage(R.string.dialog_post_notification_message)
                    setPositiveButton(R.string.dialog_ok) { dialogInterface, _ ->
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            dialogInterface.dismiss()
                        } else {
                            startSettingAppForNotificationPermission()
                        }
                    }.show()
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

        binding.search.setOnCloseListener {
            viewModel.initCgvMovieList(viewModel.cgvOrder.value)
            return@setOnCloseListener false
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

    private fun startSettingAppForNotificationPermission() {
        val notificationSettingIntent = Intent()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationSettingIntent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            notificationSettingIntent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
            notificationSettingIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        } else {
            notificationSettingIntent.action = "android.settings.APP_NOTIFICATION_SETTINGS"
            notificationSettingIntent.putExtra("app_package", requireContext().packageName)
            notificationSettingIntent.putExtra("app_uid", requireContext().applicationInfo?.uid)
            notificationSettingIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        startActivity(notificationSettingIntent)
    }
}