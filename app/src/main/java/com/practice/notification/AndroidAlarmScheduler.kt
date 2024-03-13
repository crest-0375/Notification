package com.practice.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalTime
import java.util.Calendar

class AndroidAlarmScheduler {

    companion object {
        const val ALARM_TYPE_RTC = 100
        const val PENDING_INTENT = 101 //used for old repeating alarms
        const val EXACT_ALARM_REQUEST_CODE = 102 //new request code for exact alarms
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setNotificationTime(context: Context, alarmTime: LocalTime) {
        cancelOldAlarms(context)

        val alarmIntent = PendingIntent.getBroadcast(
            context,
            EXACT_ALARM_REQUEST_CODE,
            Intent(context, AlarmReceiver::class.java),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmTimeCal = if (LocalTime.now().isAfter(alarmTime)) {
            Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, alarmTime.hour)
                set(Calendar.MINUTE, alarmTime.minute)
                add(Calendar.MINUTE, 1)
            }
        } else {
            Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, alarmTime.hour)
                set(Calendar.MINUTE, alarmTime.minute)
            }
        }
        val receiver = ComponentName(context, NotificationResetReceiver::class.java)
        context.packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTimeCal.timeInMillis,
            alarmIntent
        )
    }

    private fun cancelOldAlarms(context: Context) {
        val pendingIntent =
            PendingIntent.getBroadcast(
                context,
                PENDING_INTENT,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )

        pendingIntent.cancel()
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        Log.d("TAG", "intent canceled")
    }
}