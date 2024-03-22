package com.wakeupdev.joyfin.utils

import com.wakeupdev.joyfin.models.Transaction
import com.wakeupdev.joyfin.models.networkres.Transaction as NetworkTransaction

object ModelUtils {
    fun networkTransactionToModel(
        networkTransaction: List<NetworkTransaction>,
        saverType: String
    ): List<Transaction> {
        return networkTransaction.map {
            Transaction(
                it.amount,
                it.type ?: "",
                it.narration ?: "",
                it.dateCreated ?: "",
                saverType
            )
        }.toList()
    }
}