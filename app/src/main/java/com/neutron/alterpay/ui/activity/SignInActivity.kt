package com.neutron.alterpay.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.neutron.alterpay.databinding.ActivitySignInBinding
import com.neutron.alterpay.ui.adapter.SignInActivityPagerAdapter

class SignInActivity : AppCompatActivity() {
    private lateinit var signInBinding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signInBinding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(signInBinding.root)

        val viewPager: ViewPager = signInBinding.viewPager
        viewPager.adapter = SignInActivityPagerAdapter(supportFragmentManager)
        signInBinding.tabs.setupWithViewPager(viewPager)
    }
}