package kr.co.helicopark.movienoti.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kr.co.helicopark.movienoti.R
import kr.co.helicopark.movienoti.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (!isGranted) {
            androidx.appcompat.app.AlertDialog.Builder(this).apply {
                setCancelable(false)
                setTitle(R.string.dialog_default_title)
                setMessage(R.string.dialog_reject_post_notification_message)
                setPositiveButton(R.string.dialog_setting) { dialogInterface, _ ->
                    startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:${packageName}")))
                    dialogInterface.dismiss()
                }
                setNegativeButton(R.string.dialog_cancel) { _, _ -> }
            }.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigationBar()
        createChannel()
        requestNotificationPermission()

        viewModel.checkUserInfo()

        lifecycleScope.launch {
            viewModel.userInfoStatus.collect {

            }
        }
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.movie_channel_id)
            val channelName = getString(R.string.movie_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH,
                ),
            )
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) !=
                PackageManager.PERMISSION_GRANTED
            ) {
                androidx.appcompat.app.AlertDialog.Builder(this).apply {
                    setCancelable(false)
                    setTitle(R.string.dialog_default_title)
                    setMessage(R.string.dialog_post_notification_message)
                    setPositiveButton(R.string.dialog_ok) { dialogInterface, _ ->
                        requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                        dialogInterface.dismiss()
                    }.show()
                }
            }
        }
    }

    private fun initNavigationBar() {
        val navView = binding.navigation
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.frame_fragment) as NavHostFragment

        val navController = navHostFragment.findNavController()
        navView.setupWithNavController(navController)
    }
}