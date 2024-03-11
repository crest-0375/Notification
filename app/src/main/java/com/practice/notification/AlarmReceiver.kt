package com.practice.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.practice.notification.R.*


class AlarmReceiver : BroadcastReceiver() {
    var MID = 0
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return
        println("Alarm triggered: $message")
        Log.d("TAG", message)
        if (context != null) {
            createNotification(context)
        }
    }

    private fun createNotification(context: Context) {
        val notificationManager = context
            .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationIntent = Intent(
            context,
            MainActivity::class.java
        )
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(
            context, 0,
            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )


        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val mNotifyBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
            context
        ).setSmallIcon(drawable.notification_icon)
            .setContentTitle("Alarm Fired")
            .setContentText("Events to be Performed").setSound(alarmSound)
            .setAutoCancel(true).setWhen(System.currentTimeMillis())
            .setContentIntent(pendingIntent)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
        notificationManager.notify(MID, mNotifyBuilder.build())
        MID++
    }

}