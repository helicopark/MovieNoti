package kr.co.helicopark.movienoti.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        startMovieApp(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigationBar()
        createChannel()

        viewModel.checkUserInfo()
        viewModel.checkUpdate()

        lifecycleScope.launch {
            launch {
                viewModel.forcedUpdate.collect { message ->
                    AlertDialog.Builder(this@MainActivity).apply {

                        val updateMessage = message.ifEmpty {
                            getString(R.string.dialog_update_forced_message)
                        }

                        setTitle(R.string.dialog_default_title)
                        setMessage(updateMessage)
                        setCancelable(false)
                        setPositiveButton(R.string.dialog_update) { _, _ ->
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=kr.co.helicopark.movienoti")))
                            finishAffinity()
                        }

                        show()
                    }
                }
            }

            launch {
                viewModel.optionalUpdate.collect { message ->
                    AlertDialog.Builder(this@MainActivity).apply {

                        val updateMessage = message.ifEmpty {
                            getString(R.string.dialog_update_optional_message)
                        }

                        setTitle(R.string.dialog_default_title)
                        setMessage(updateMessage)
                        setCancelable(false)
                        setPositiveButton(R.string.dialog_update) { _, _ ->
                            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=kr.co.helicopark.movienoti")))
                        }

                        setNegativeButton(R.string.dialog_cancel) { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }

                        show()
                    }
                }
            }
        }

        startMovieApp(intent)
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

    private fun initNavigationBar() {
        val navView = binding.navigation
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.frame_fragment) as NavHostFragment

        val navController = navHostFragment.findNavController()
        navView.setupWithNavController(navController)
    }

    private fun startMovieApp(intent: Intent?) {
        val brand = intent?.extras?.getString("brand") ?: ""
        if (brand.isNotBlank()) {
            if (brand == "CGV") {
                AlertDialog.Builder(this@MainActivity).apply {
                    setTitle(R.string.dialog_reservation_title)
                    setMessage("${intent?.extras?.getString("text") ?: ""}\nCGV앱으로 이동할까요?")
                    setPositiveButton(R.string.dialog_ok) { _, _ ->
                        val cgvIntent = Intent(Intent.ACTION_VIEW, Uri.parse("cjcgv://"))
                        startActivity(cgvIntent)
                    }
                    setNegativeButton(R.string.dialog_cancel, null)
                    show()
                }
            }
        }
    }
}