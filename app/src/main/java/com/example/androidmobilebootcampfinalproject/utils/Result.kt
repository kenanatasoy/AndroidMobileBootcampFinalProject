package com.example.androidmobilebootcampfinalproject.utils

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    object Error : Result<Nothing>()
    object Loading : Result<Nothing>()
}

