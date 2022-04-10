package com.neutron.alterpay.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.neutron.alterpay.ui.fragment.LoginFragment
import com.neutron.alterpay.ui.fragment.RegisterFragment

class SignInActivityPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> LoginFragment.newInstance()
            else -> RegisterFragment.newInstance()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> "Login"
            else -> "Register"
        }
    }
}