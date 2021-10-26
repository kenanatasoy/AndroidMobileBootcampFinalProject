package com.example.androidmobilebootcampfinalproject.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.androidmobilebootcampfinalproject.base.ScreenActions
import com.example.androidmobilebootcampfinalproject.databinding.ActivityHomeBinding
import com.example.androidmobilebootcampfinalproject.network.ConnectionLiveData2
import com.example.androidmobilebootcampfinalproject.utils.showToast
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class HomeActivity : AppCompatActivity(), ScreenActions, KoinComponent {

    private lateinit var activityMainBinding: ActivityHomeBinding

    private val connectionLiveData2: ConnectionLiveData2 by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityMainBinding = ActivityHomeBinding.inflate(layoutInflater)
        val view = activityMainBinding.root
        setContentView(view)

        shouldCheckInternetConnection()

    }


    override fun shouldCheckInternetConnection() {

        connectionLiveData2.observe(this@HomeActivity, { isConnected ->

            if (!isConnected){
                showToast("No internet connection")
            }
            else {
                showToast("Internet connection is on")
            }

        })

    }


}