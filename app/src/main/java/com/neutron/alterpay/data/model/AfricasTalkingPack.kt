package com.neutron.alterpay.data.model

import com.google.gson.annotations.SerializedName

data class AfricasTalkingPack(
    @SerializedName("username" ) var username : String? = "sandbox",
    @SerializedName("to"       ) var to       : String? = null,
    @SerializedName("message"  ) var message  : String? = null
)