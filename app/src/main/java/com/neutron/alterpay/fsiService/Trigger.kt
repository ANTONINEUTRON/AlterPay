package com.neutron.alterpay.fsiService

import com.neutron.alterpay.data.model.AfricasTalkingPack
import com.neutron.alterpay.data.model.FlutterwavePack

object Trigger {
    private val fsiEndpoints: FsiEndpoints = RetrofitService.getRetrofitInstance().create(com.neutron.alterpay.fsiService.FsiEndpoints::class.java)

    fun sendSms(to: String, Message: String){
        val africasTalkingPack = AfricasTalkingPack(to = to, message = null)
        fsiEndpoints.sendSmsThroughAfricastalking(africasTalkingPack)
    }

    fun chargeCard(){
        fsiEndpoints.chargeCard(FlutterwavePack())
    }
}