package com.example.androidmobilebootcampfinalproject.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class BaseFragment<VM : ViewModel?, DB : ViewDataBinding?> : Fragment() {

    abstract val viewModel: VM?
    protected abstract var _dataBinding: DB?

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _dataBinding = DataBindingUtil.inflate(inflater, getLayoutID(), container, false)
        return _dataBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareView()
        observeLiveData()
    }

    abstract fun getLayoutID(): Int
    abstract fun observeLiveData()
    abstract fun prepareView()


    override fun onDestroyView() {
        super.onDestroyView()
        _dataBinding = null
    }

}

