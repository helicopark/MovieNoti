package kr.co.helicopark.movienoti.ui.setting

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kr.co.helicopark.movienoti.databinding.FragmentSettingBinding

@AndroidEntryPoint
class SettingFragment : Fragment() {
    private lateinit var binding: FragmentSettingBinding
    private val viewModel: SettingViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (!isGranted) {
            val notificationSettingIntent = Intent()
            notificationSettingIntent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
            notificationSettingIntent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
            notificationSettingIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(notificationSettingIntent)
        }
    }

    override fun onResume() {
        super.onResume()
        binding.switchSettingNotification.isChecked = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewSettingNotification.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    val notificationSettingIntent = Intent()
                    notificationSettingIntent.action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
                    notificationSettingIntent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                    notificationSettingIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(notificationSettingIntent)
                }
            } else {
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

        binding.clSettingReview.setOnClickListener {
            requireContext().startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=kr.co.helicopark.movienoti")))
        }
    }
}