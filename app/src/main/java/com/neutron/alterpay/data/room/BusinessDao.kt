package com.neutron.alterpay.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.neutron.alterpay.data.model.Business

@Dao
interface BusinessDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createBusiness(business: Business)

    @Query("select * from business limit 1")
    fun getBusiness(): LiveData<Business>

    @Query("select * from business limit 1")
    fun getBusinessObject(): Business
}