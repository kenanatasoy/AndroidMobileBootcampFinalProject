package com.example.androidmobilebootcampfinalproject.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

    inline fun<reified T : AppCompatActivity> Context.startActivity(block : Intent.() -> Unit = {}){
        val intent  = Intent(this , T::class.java)
        startActivity(
            intent.also {
                block.invoke(it)
            }
        )
    }

    inline fun FragmentManager.startFragment(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }

    fun Fragment.changeFragmentStatusBarColor(id: Int) {
        activity?.window?.statusBarColor = resources.getColor(id)
    }

    fun Activity.changeActivityStatusBarColor(id: Int){
        window?.statusBarColor = resources.getColor(id)
    }

    fun append(arr: Array<String>, element: String): Array<String> {
        val list: MutableList<String> = arr.toMutableList()
        list.add(element)
        return list.toTypedArray()
    }

    fun Fragment.showToast(messageToShow : String, length: Int){
        Toast.makeText(requireContext(), messageToShow, length).show()
    }

    fun Activity.showToast(messageToShow : String){
        Toast.makeText(this, messageToShow, Toast.LENGTH_SHORT).show()
    }
