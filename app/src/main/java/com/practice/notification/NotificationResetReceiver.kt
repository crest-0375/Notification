package com.practice.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime

class NotificationResetReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action.equals("android.intent.action.BOOT_COMPLETED") ||
            intent?.action.equals("android.intent.action.TIMEZONE_CHANGED") ||
            intent?.action.equals("android.intent.action.TIME_SET")) {
            val notificationScheduler = AndroidAlarmScheduler()
            notificationScheduler.setNotificationTime(context, LocalTime.now().plusSeconds(15))
        }
    }
}
