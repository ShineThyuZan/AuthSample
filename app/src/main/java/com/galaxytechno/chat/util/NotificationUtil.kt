package com.galaxytechno.chat.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.galaxytechno.chat.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
Android 4.0 (API 14)                     ->     Notification introduced.
From Android 4.0 to Android 7.1 (API 25) ->     Notifications are on per-app ( only 1 channel in 1 app )
From Android 8.0 ( API 26 )              ->     All notifications are assigned with a CHANNEL, if not they will not appeared.
 */

class NotificationUtil @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {

        private const val CHANNEL_ID = "galaxy_techno.seller"
        private const val CHANNEL_NAME = "galaxy_techno.seller.notification_channel"
        const val DEFAULT_CHANNEL_ID = "Foreground Service"

    }

    // default channel for Android 4 and higher ( foreground service )
    fun createDefaultChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            val name = "galaxy_techno.seller.channel"
            val descriptionText = "foreground"
            val channel = NotificationChannel(
                DEFAULT_CHANNEL_ID,
                name,
                NotificationManager.IMPORTANCE_LOW
            )
            channel.description = descriptionText
            val notificationManager: NotificationManager =
                context.getSystemService(NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    //create the NotificationChannel, API 26+ (from Android 8 Oreo) we can manage the visual and auditory behavior to all notifications in the channel
    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = "galaxy_techno.seller.channel"
            val descriptionText = "Android 8 and higher description"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance)

            channel.apply {
                description = descriptionText
                lightColor = Color.BLUE
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                vibrationPattern = (longArrayOf(1000L, 1000L))
            }

            channel.description = descriptionText
            channel.enableLights(true)
            channel.lightColor = Color.BLUE
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            channel.enableVibration(true)
            channel.setSound(
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build()
            )
            channel.vibrationPattern = (longArrayOf(1000, 1000))


            // Register the channel with the system
            val notificationManager: NotificationManager =
                context.getSystemService(NotificationManager::class.java) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    //assign notification content
    fun showNotification(msg: String = "", pendingIntent: PendingIntent? = null) {
        createNotificationChannel()


        //NotificationCompat with Android 8.0 (API level 26) and higher, but is ignored by older versions.
        val largeIcon = ContextCompat.getDrawable(context,R.drawable.ic_undraw_welcome)?.toBitmap()
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
        val id = System.currentTimeMillis().toInt()
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_undraw_welcome)
            .setSound(sound)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(msg)
            .setLargeIcon(largeIcon)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(largeIcon))
            .setAutoCancel(true)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        pendingIntent?.let {
            builder.setContentIntent(it)
        }

        //notification content below Android N (API 24)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            builder.priority = NotificationCompat.PRIORITY_HIGH
            builder.setSound(sound)
            builder.setVibrate(longArrayOf(100, 100))
            builder.setLights(Color.BLUE, 0, 1)
            builder.setSmallIcon(R.drawable.ic_undraw_welcome)
        }

        // It won't show "Heads Up" unless it plays a sound
        builder.setVibrate(
            longArrayOf(
                100,
                100
            )
        )

        with(NotificationManagerCompat.from(context)) {
            notify(id, builder.build())
        }
    }

    fun clearAllNotification() {
        NotificationManagerCompat.from(context).cancelAll()
    }

}