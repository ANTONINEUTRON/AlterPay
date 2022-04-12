package com.neutron.alterpay.ui.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.neutron.alterpay.data.model.Business
import com.neutron.alterpay.databinding.FragmentLoginBinding
import com.neutron.alterpay.fsiService.RetrofitService
import com.neutron.alterpay.fsiService.Trigger
import com.neutron.alterpay.ui.activity.MainActivity
import com.neutron.alterpay.ui.viewModel.SiginViewModel
import com.neutron.alterpay.utils.Alert


class LoginFragment : Fragment() {
    private lateinit var fragmentLoginBinding: FragmentLoginBinding
    private lateinit var signinViewModel: SiginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        signinViewModel = ViewModelProvider(this).get(SiginViewModel::class.java)
        // Inflate the layout for this fragment
        fragmentLoginBinding = FragmentLoginBinding.inflate(inflater,container,false)
        return fragmentLoginBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragmentLoginBinding.loginButton.setOnClickListener{
            //check if phone number and password match
            val phoneNumber = fragmentLoginBinding.phoneNumber.editText?.text.toString()
            val password = fragmentLoginBinding.password.editText?.text.toString()
            if(!phoneNumber.isNullOrEmpty() && !password.isNullOrEmpty() && isLoginDetailsValid(phoneNumber,password)){
                startActivity(Intent(requireContext(), MainActivity::class.java))
                Trigger.sendSms("+234${phoneNumber.subSequence(1,phoneNumber.length-1)}","You just logged into your alterpay account")
                requireActivity().finish()
            }else{
                Alert.show(requireContext(), "Invalid Login Details")
            }
        }

        fragmentLoginBinding.forgotPassword.setOnClickListener {
            //send sms to the phone number inserted
            val business: Business = signinViewModel.getBusinessDetails()
            if(business == null){
                Alert.show(requireContext(), "Register an account")
            }else{
                Trigger.sendSms("+234${business.phoneNumber.subSequence(1,business.phoneNumber.length-1)}",
                    "Your password is ${business.password}\n Endeavour to change your password to what you can remember")
                val alert = AlertDialog.Builder(requireContext())
                alert.apply {
                    setMessage("An SMS with your password has been sent to the phone number you used in registering")
                    setNeutralButton("OK",null)
                }
            }
        }
    }

    private fun isLoginDetailsValid(phoneNumber: String, password: String): Boolean {
        return signinViewModel.isUserDetailsValid(phoneNumber, password)
    }

    companion object {
        fun newInstance() = LoginFragment()
    }
}