package com.practice.notification

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDateTime

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        val scheduler = AndroidAlarmScheduler(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val alarmItem = AlarmItem(
            LocalDateTime.now().plusSeconds(15),
            "Hello jemish"
        )
        alarmItem.let(scheduler::schedule)

    }
}