package com.neutron.alterpay.ui.fragment

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModelProvider
import com.neutron.alterpay.R
import com.neutron.alterpay.data.model.Business
import com.neutron.alterpay.databinding.FragmentRegisterBinding
import com.neutron.alterpay.fsiService.Trigger
import com.neutron.alterpay.ui.activity.SignInActivity
import com.neutron.alterpay.ui.viewModel.SiginViewModel
import com.neutron.alterpay.utils.Alert

class RegisterFragment : Fragment() {
    private lateinit var registerBinding: FragmentRegisterBinding
    private lateinit var signinViewModel: SiginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        signinViewModel = ViewModelProvider(this).get(SiginViewModel::class.java)
        // Inflate the layout for this fragment
        registerBinding = FragmentRegisterBinding.inflate(inflater, container, false)
        return registerBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerBinding.registerBtn.setOnClickListener {
            //Get values and store in db
            var areValuesValid = true
            val phoneNumber = registerBinding.phoneNumber.editText?.text.toString()
            if(phoneNumber.isNullOrEmpty() || phoneNumber.length < 10 || !phoneNumber.isDigitsOnly()){
                areValuesValid = false
                registerBinding.phoneNumber.error ="Enter a valid phone number"
            }
            val businessName = registerBinding.businessName.editText?.text.toString()
            if (businessName.isNullOrEmpty() || businessName.length < 2){
                areValuesValid = false
                registerBinding.businessName.error = "Enter a valid business name"
            }
            var email =  registerBinding.email.editText?.text.toString()
            if(email.isNullOrEmpty()){
                email = ""
            }
            val password = registerBinding.password.editText?.text.toString()
            val confirmPassword =  registerBinding.confirmPassword.editText?.text.toString()
            if(password.length < 5 || password != confirmPassword){
                areValuesValid = false
                registerBinding.password.error = "Password should match the confirm password"
            }
            val business = Business(phoneNumber= phoneNumber,name = businessName,email = email, password = password.toLowerCase())
            if (areValuesValid){
                signinViewModel.registerBusiness(business)
                val alertDialog = AlertDialog.Builder(requireContext())

                alertDialog.apply {
                    setMessage("Account registered successfully")
                    setNeutralButton("Ok", DialogInterface.OnClickListener { dialogInterface, i ->
                        startActivity(Intent(requireContext(), SignInActivity::class.java))
                        Trigger.sendSms("+234${phoneNumber.subSequence(1,phoneNumber.length-1)}", "Your Alterpay account has been created successfully")
                        requireActivity().finish()
                    })
                    show()
                }
            }
        }
    }

    companion object {
        fun newInstance() = RegisterFragment()
    }
}