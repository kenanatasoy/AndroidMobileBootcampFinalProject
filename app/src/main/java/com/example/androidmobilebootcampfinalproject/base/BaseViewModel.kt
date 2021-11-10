package com.example.androidmobilebootcampfinalproject.base

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    abstract val repository: OneRepository

}