package com.neutron.alterpay.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.neutron.alterpay.R
import com.neutron.alterpay.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var splashBinding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        splashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)

        val blinkAnim: Animation = AnimationUtils.loadAnimation(this, R.anim.blink)
        splashBinding.title.animation = blinkAnim
        splashBinding.description.animation = blinkAnim

        splashBinding.signinBtn.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }

    }
}