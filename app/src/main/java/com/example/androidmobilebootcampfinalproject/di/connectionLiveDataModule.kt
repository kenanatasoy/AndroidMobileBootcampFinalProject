package com.example.androidmobilebootcampfinalproject.di

import com.example.androidmobilebootcampfinalproject.network.ConnectionLiveData2
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

var connectionLiveDataModule = module {

    single { ConnectionLiveData2(this.androidContext()) }

}