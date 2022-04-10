package com.neutron.alterpay.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.neutron.alterpay.data.model.Business
import com.neutron.alterpay.data.room.AppDatabase

class OTPViewModel(application: Application): AndroidViewModel(application) {
    private val businessDao = AppDatabase.getDatabase(application.baseContext).businessDao()
    private val business: Business = businessDao.getBusinessObject()
    fun updateBalance(amount: Float) {
        var bal = business.balance
        business.balance = bal + amount
        businessDao.createBusiness(business)
    }
}