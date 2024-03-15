package kr.co.helicopark.movienoti.data.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kr.co.helicopark.movienoti.R
import kr.co.helicopark.movienoti.ui.MainActivity

class MessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        // 토큰 갱신, 현재 예약 중인 영화 토큰 변경
    }

    override fun onMessageReceived(message: RemoteMessage) {
        sendNotification(message.data)
    }

    private fun sendNotification(message: Map<String, String>) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        intent.putExtra("text", message["text"])
        intent.putExtra("brand", message["brand"])
//        areaCode
//        movieFormat
//        reservationDate
//        text
//        brand
//        theaterCode
//        movieTitle

        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_IMMUTABLE,
        )

        val channelId = message["channel_id"] ?: getString(R.string.movie_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.icon_notification)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(message["text"])
            .setSubText(message["subText"])
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                getString(R.string.movie_channel_name),
                NotificationManager.IMPORTANCE_HIGH,
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
    }
}