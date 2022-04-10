package com.neutron.alterpay.ui.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.neutron.alterpay.data.model.Business
import com.neutron.alterpay.data.room.AppDatabase

class ProfileViewModel(application: Application):AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application.baseContext)

    fun getBusinessDetails(): LiveData<Business> {
        return db.businessDao().getBusiness()
    }

    fun updateBusiness(business: Business) {
        db.businessDao().createBusiness(business)
    }
}