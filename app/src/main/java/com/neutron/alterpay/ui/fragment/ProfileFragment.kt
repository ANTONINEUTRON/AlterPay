package com.neutron.alterpay.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.neutron.alterpay.R
import com.neutron.alterpay.data.model.Business
import com.neutron.alterpay.databinding.FragmentLoginBinding
import com.neutron.alterpay.databinding.FragmentProfileBinding
import com.neutron.alterpay.ui.viewModel.ProfileViewModel
import com.neutron.alterpay.utils.Alert

class ProfileFragment : Fragment() {
    private lateinit var profileViewModel: ProfileViewModel
    private lateinit var profileBinding: FragmentProfileBinding
    private lateinit var business: Business

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        profileBinding = FragmentProfileBinding.inflate(inflater, container, false)

        return profileBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        profileViewModel.getBusinessDetails().observe(viewLifecycleOwner, Observer { business ->
            this.business = business
            profileBinding.businessName.text = business.name
            profileBinding.balance.text = "Balance: NGN"+business.balance.toString()
            profileBinding.withdrawBtn.setOnClickListener {
                Alert.show(requireContext(), "This is an MVP, withdrawal will be supported later")
            }
            profileBinding.changePassword.setOnClickListener {
                val alertDialog = AlertDialog.Builder(requireContext()).create()
                val view = layoutInflater.inflate(R.layout.change_password_layout, null)
                view.findViewById<MaterialButton>(R.id.change_password_btn).setOnClickListener {
                    val oldPassword: String = view.findViewById<TextInputLayout>(R.id.old_password).editText?.text.toString()
                    val newPassword: String = view.findViewById<TextInputLayout>(R.id.new_password).editText?.text.toString()
                    if(oldPassword == business.password){
                        if (!newPassword.isNullOrEmpty() || newPassword.length < 4) {
                            business.password = newPassword
                            alertDialog.cancel()
                        }else{
                            Alert.show(requireContext(),"Your new password is not strong enough")
                        }
                    }else{
                        Alert.show(requireContext(), "The old password doesn't match!")
                    }
                }


                alertDialog.apply {
                    setView(view)
                    show()
                }
            }
            profileBinding.phoneNumber.editText?.setText(business.phoneNumber)
            profileBinding.email.editText?.setText(business.email)
            profileBinding.accountName.editText?.setText(business.accountName)
            profileBinding.accountNumber.editText?.setText(business.accountNumber)
            profileBinding.bankName.editText?.setText(business.bankName)
        })
    }

    override fun onPause() {
        //Save changed items
        business.phoneNumber = profileBinding.phoneNumber.editText?.text.toString()
        business.email = profileBinding.email.editText?.text.toString()
        business.accountName = profileBinding.accountName.editText?.text.toString()
        business.accountNumber = profileBinding.accountNumber.editText?.text.toString()
        business.bankName = profileBinding.bankName.editText?.text.toString()

        profileViewModel.updateBusiness(business)
        super.onPause()
    }
    companion object{
        fun newInstance() = ProfileFragment()
    }
}