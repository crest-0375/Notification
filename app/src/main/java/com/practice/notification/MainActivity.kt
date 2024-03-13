package com.practice.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.time.LocalTime

class MainActivity : AppCompatActivity() {

    companion object {
        const val CHANNEL_ID = "Presently Gratitude Reminder"
        const val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001
        private const val SCHEDULE_ALARM_PERMISSION_REQUEST_CODE = 1002
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestScheduleAlarmPermission()
        }

        createNotificationChannels()

        AndroidAlarmScheduler().setNotificationTime(this, LocalTime.now().plusSeconds(15))
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestScheduleAlarmPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SCHEDULE_EXACT_ALARM
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.SCHEDULE_EXACT_ALARM,
                    Manifest.permission.USE_EXACT_ALARM
                ),
                SCHEDULE_ALARM_PERMISSION_REQUEST_CODE
            )
        } else {
            Toast.makeText(this, "alarm Permission granted", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Presently Reminder",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannels(listOf(notificationChannel))
        }
    }

}