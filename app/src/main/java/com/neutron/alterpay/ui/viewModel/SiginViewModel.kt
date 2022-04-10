package com.neutron.alterpay.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.neutron.alterpay.data.model.Business
import com.neutron.alterpay.data.room.AppDatabase

class SiginViewModel(application: Application): AndroidViewModel(application) {
    private val database: AppDatabase = AppDatabase.getDatabase(application.baseContext)
    val businessDetail: Business = database.businessDao().getBusinessObject()

    fun isUserDetailsValid(phoneNumber: String, password: String): Boolean {
        return businessDetail.phoneNumber == phoneNumber && businessDetail.password == password.toLowerCase()
    }

    fun getBusinessOwnerDetails(): LiveData<Business> {
        return database.businessDao().getBusiness()
    }

    fun getBusinessDetails(): Business {
        return businessDetail
    }

    fun registerBusiness(business: Business) {
        database.businessDao().createBusiness(business)
    }

}