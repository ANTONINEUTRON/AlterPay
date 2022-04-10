package com.neutron.alterpay.utils

import android.content.Context
import android.widget.Toast

object Alert {
    fun show(context: Context, message: String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}