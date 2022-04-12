package com.neutron.alterpay.fsiService

import android.content.Context
import android.widget.Toast
import com.neutron.alterpay.data.model.AfricasTalkingPack
import com.neutron.alterpay.data.model.FlutterwavePack
import com.neutron.alterpay.utils.Alert
import okhttp3.Callback
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response

object Trigger {
    private val fsiEndpoints: FsiEndpoints = RetrofitService.getRetrofitInstance().create(
        FsiEndpoints::class.java)

    fun sendSms(to: String, Message: String){
        val africasTalkingPack = AfricasTalkingPack(to = to, message = null)
        fsiEndpoints.sendSmsThroughAfricastalking(africasTalkingPack)
            .enqueue(object: retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                }

            })
    }

    fun chargeCard(){
        fsiEndpoints.chargeCard(FlutterwavePack())
            .enqueue(object: retrofit2.Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                }

            })
    }
}