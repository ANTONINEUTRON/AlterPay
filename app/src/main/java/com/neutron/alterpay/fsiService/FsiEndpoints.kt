package com.neutron.alterpay.fsiService

import com.neutron.alterpay.data.model.AfricasTalkingPack
import com.neutron.alterpay.data.model.FlutterwavePack
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FsiEndpoints {
    @Headers("Content-Type: application/json","sandbox-key: 2JuUclGZ2Pd0dUfrgwZGg5PD1YHdByrU1649586645")
    @POST("africastalking/version1/messaging")
    fun sendSmsThroughAfricastalking(@Body africasTalkingPack: AfricasTalkingPack): Call<Void>

    @Headers("Content-Type: application/json",
        "sandbox-key: 2JuUclGZ2Pd0dUfrgwZGg5PD1YHdByrU1649586645",
        "Authorization: dskjdks")
    @POST("flutterwave/v3/charges?type=card")
    fun chargeCard(@Body flutterwavePack: FlutterwavePack): Call<Void>//Due to simulation constraint no point handling response
}