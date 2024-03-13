package com.practice.notification

import android.Manifest
import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.practice.notification.AndroidAlarmScheduler.Companion.ALARM_TYPE_RTC
import com.practice.notification.MainActivity.Companion.CHANNEL_ID
import java.time.LocalTime


class AlarmReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent) {
        val openActivityIntent = Intent(context, MainActivity::class.java)
        openActivityIntent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TOP //set flag to restart/relaunch the app
        openActivityIntent.putExtra(fromNotification, true)
        val pendingIntent = PendingIntent.getActivity(
            context,
            ALARM_TYPE_RTC,
            openActivityIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        if (context != null) {
            createLocalNotification(context, pendingIntent)

            AndroidAlarmScheduler().setNotificationTime(context, LocalTime.now().plusSeconds(15))
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun createLocalNotification(context: Context, pendingIntent: PendingIntent) {
        val title = "Presently Gratitude Reminder"
        val content = "What are you thankful for today? ${LocalTime.now()}"

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setCategory(Notification.CATEGORY_REMINDER)
            .setAutoCancel(true)

        val notificationManager = NotificationManagerCompat.from(context)

        notificationManager.notify(ALARM_TYPE_RTC, notificationBuilder.build())
    }

    companion object {
        const val fromNotification = "fromNotification"
    }
}