package com.neutron.alterpay.ui.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.neutron.alterpay.data.model.CardDetail
import com.neutron.alterpay.databinding.FragmentOtpBinding
import com.neutron.alterpay.fsiService.Trigger
import com.neutron.alterpay.ui.activity.MainActivity
import com.neutron.alterpay.ui.viewModel.OTPViewModel

class OtpFragment(val cardDetail: CardDetail) : BottomSheetDialogFragment() {
    private lateinit var otpViewModel: OTPViewModel
    private lateinit var otpBinding: FragmentOtpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        otpViewModel = ViewModelProvider(this).get(OTPViewModel::class.java)
        otpBinding = FragmentOtpBinding.inflate(inflater,container, false)
        // Inflate the layout for this fragment
        return otpBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        showTransactionMsg()
        otpBinding.completeTrans.setOnClickListener {
            val otp: String = otpBinding.otpEdittext.text.toString()
            if(otp.length == 4 && otp.isDigitsOnly()){
                val alertDialog = AlertDialog.Builder(requireContext())
                alertDialog.apply {
                    Trigger.chargeCard()
                    otpViewModel.updateBalance(cardDetail.amount)
                    //TODO update DB
                    setMessage("TRANSACTION SUCCESSFUL!")
                    setNeutralButton("Ok",DialogInterface.OnClickListener { dialogInterface, i ->
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                        activity?.finish()
                    })
                    show()
                }
            }
        }
    }

    private fun showTransactionMsg() {
        val message = "Transaction Initiated: \n Amount: ${cardDetail.amount} \n " +
                "Customer Name: ${cardDetail.cardName} \n " +
                "Card Number: ${cardDetail.cardNumber} \n \n" +
                "An OTP has been sent to ${cardDetail.phoneNumber}"
        otpBinding.transactionText.setText(message)

    }
}