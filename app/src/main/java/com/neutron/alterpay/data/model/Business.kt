package com.neutron.alterpay.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Business(
    @PrimaryKey
    var phoneNumber: String,
    val name: String,
    var email: String,
    var password: String,
    var balance: Float = 0.0f,
    var accountName: String = "",
    var accountNumber: String = "",
    var bankName: String = ""
)
