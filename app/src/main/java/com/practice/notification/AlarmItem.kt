package com.practice.notification

import java.time.LocalDateTime

data class AlarmItem(
    val time: LocalDateTime,
    val message: String
)