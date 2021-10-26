package com.example.androidmobilebootcampfinalproject.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidmobilebootcampfinalproject.databinding.ActivitySplashBinding
import com.example.androidmobilebootcampfinalproject.ui.HomeActivity
import com.example.androidmobilebootcampfinalproject.utils.startActivity
import java.util.*

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var activitySplashBinding: ActivitySplashBinding

    private val ONSCREENSTAYDELAY  :  Long = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        val view = activitySplashBinding.root
        setContentView(view)

        Timer().schedule(object : TimerTask() {
            override fun run() {
                startActivity<HomeActivity>{}
                finish()
                // starting the splash activity on create and keeping it on the screen for 1 second
            }
        }, ONSCREENSTAYDELAY)

//        startActivity<HomeActivity>{}
//        finish()

    }

}