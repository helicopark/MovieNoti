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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kr.co.helicopark.movienoti.R
import kr.co.helicopark.movienoti.databinding.FragmentCgvBinding
import kr.co.helicopark.movienoti.domain.model.UiStatus
import kr.co.helicopark.movienoti.ui.bottom.MovieBottomFragment
import kr.co.helicopark.movienoti.ui.datePicker
import kr.co.helicopark.movienoti.ui.model.CgvMovieItem


@AndroidEntryPoint
class CgvFragment : Fragment() {
    private lateinit var binding: FragmentCgvBinding
    private val viewModel: CgvViewModel by viewModels()

    private var selectedMovieItem: CgvMovieItem = CgvMovieItem("", "", "", "")

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (!isGranted) {
            AlertDialog.Builder(requireContext()).apply {
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
            showDatePicker(selectedMovieItem)
        }
    }

    private val adapter by lazy {
        CgvAdapter { movieItem ->
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                showDatePicker(movieItem)
            } else {
                selectedMovieItem = movieItem

                AlertDialog.Builder(requireContext()).apply {
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.cgvMovieList.collectLatest {
                if (it.state == UiStatus.ERROR) {
                    AlertDialog.Builder(requireContext()).apply {
                        setTitle(R.string.dialog_default_title)
                        setMessage(it.message)
                        setCancelable(false)
                        setPositiveButton(R.string.dialog_retry) { dialogInterface, _ ->
                            viewModel.initCgvMovieList(viewModel.cgvOrder.value)
                            dialogInterface.dismiss()
                        }
                        setNegativeButton(R.string.dialog_finish) { dialogInterface, _ ->
                            dialogInterface.dismiss()
                            requireActivity().finishAffinity()
                        }
                    }.show()

                }
            }
        }
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

    private fun showDatePicker(movieItem: CgvMovieItem) {
        datePicker(movieItem.title).apply {
            addOnPositiveButtonClickListener { reservationDate ->
                MovieBottomFragment(null).let { fragment ->
                    Bundle().let {
                        it.putString("movieTitle", movieItem.title)
                        it.putLong("reservationDate", reservationDate)
                        it.putString("thumb", movieItem.thumb)
                        it.putBoolean("isUpdate", false)
                        fragment.arguments = it
                    }

                    fragment.show(requireActivity().supportFragmentManager, "MovieBottomFragment")
                }
            }
        }.show(requireActivity().supportFragmentManager, "datePicker")
    }
}