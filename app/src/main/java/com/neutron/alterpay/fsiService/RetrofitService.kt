package com.neutron.alterpay.fsiService

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitService {
    private var retrofit: Retrofit? = null
    /**
     * This function returns instance of the Retrofit class**/
    fun getRetrofitInstance(): Retrofit{
        val BASE_URL: String = "https://fsi.ng/api/v1/"
        if(retrofit == null){
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        return retrofit!!
    }
}