package com.wakeupdev.joyfin.models

import java.time.LocalDate

data class Transaction(
    val amount: Double,
    val type: String,
    val narration: String,
    val dateCreated: String,
    val saverType: String,
)
