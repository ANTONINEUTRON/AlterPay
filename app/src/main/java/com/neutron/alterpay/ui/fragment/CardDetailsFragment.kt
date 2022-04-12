package com.neutron.alterpay.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.neutron.alterpay.data.model.CardDetail
import com.neutron.alterpay.databinding.CardDetailsLayoutBinding
import com.neutron.alterpay.utils.Alert

class CardDetailsFragment(
    val cardNumber: String="",
    val cardName: String="",
    val cvv: String="",
    val expiryDate: String=""
) : BottomSheetDialogFragment() {
    private lateinit var cardDetailsLayoutBinding:CardDetailsLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        cardDetailsLayoutBinding = CardDetailsLayoutBinding.inflate(inflater, container, false)
        return cardDetailsLayoutBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //set values to text boxes
//        detectCardNumber()
//        detectName()
//        detectCVV()
//        detectCardExpiryDate()
        cardDetailsLayoutBinding.cardNumberTextInput.editText?.setText(cardNumber)
        cardDetailsLayoutBinding.nameTextInput.editText?.setText(cardName)
        cardDetailsLayoutBinding.expiryDate.editText?.setText(expiryDate)
        cardDetailsLayoutBinding.cvv.editText?.setText(cvv)
        cardDetailsLayoutBinding.chargeCardButton.setOnClickListener{
            var areInputValid: Boolean = true
            val validationMsg = StringBuilder("")
            //get values
            val cardNumber: String = cardDetailsLayoutBinding.cardNumberTextInput.editText?.text.toString()
            if (cardNumber.length<16){
                Alert.show(requireContext(), "Card Number is invalid")
                areInputValid = false
            }
            val cVV: String = cardDetailsLayoutBinding.cvv.editText?.text.toString()
            if(cVV.length != 3){
                Alert.show(requireContext(), "CVV number is invalid")
                areInputValid = false
            }
            val expiryDate = cardDetailsLayoutBinding.expiryDate.editText?.text.toString()
            if(expiryDate.length<5){
                Alert.show(requireContext(), "Invalid Expiry Date")
                areInputValid = false
            }
//            val arrOfSplitDate: List<String> = expiryDate.split("/")
//            val expiryMonth: String = arrOfSplitDate[0]
//            val expiryYear: String = arrOfSplitDate[1]
            val amountString = cardDetailsLayoutBinding.amountTextInput.editText?.text.toString()
            val fullname: String = cardDetailsLayoutBinding.nameTextInput.editText?.text.toString()
            val amount: Float = if(!amountString.isNullOrEmpty()) amountString.toFloat() else  0f
            if (amount < 100){
                Alert.show(requireContext(), "you can't perform a transaction less than NGN 100")
                areInputValid = false
            }else if (fullname.isNullOrEmpty() || fullname.length < 3){
                Alert.show(requireContext(), "Enter a valid name")
                areInputValid = false
            }
            val ref: String = "BIZNAME_${System.currentTimeMillis()}"
            val redirectUrl: String = "" //TODO cloud function
            val email: String = cardDetailsLayoutBinding.email.editText?.text.toString()//TODO customer email if not empty
            val phoneNumber: String = cardDetailsLayoutBinding.phoneNumber.editText?.text.toString()//TODO to rceive sms from africas talking

            if (phoneNumber.isNullOrEmpty() || phoneNumber.length<11){
                Alert.show(requireContext(), "Enter a valid phone number")
            }
            //encrypt
            //pass to api
            //FOR FSI SIMULATION PURPOSE
            if(areInputValid){
                val truncateCardNum = "${cardNumber.subSequence(0,5)}******${cardNumber.subSequence(11, cardNumber.length-1)}"
                val cardDetail = CardDetail(amount, truncateCardNum, fullname, phoneNumber)
                val otpFragment = OtpFragment(cardDetail)
                otpFragment.show(parentFragmentManager, "SHOW FOR OTP")
                this.dismiss()
            }
        }
    }



    companion object{
        fun newInstance(
            cardNumber: String?,
            cardName: String?,
            cvv: String?,
            expiryDate: String?)
        = CardDetailsFragment(
            cardNumber ?: "",
            cardName ?: "",
            cvv ?: "",
            expiryDate ?: ""
        )
    }
}