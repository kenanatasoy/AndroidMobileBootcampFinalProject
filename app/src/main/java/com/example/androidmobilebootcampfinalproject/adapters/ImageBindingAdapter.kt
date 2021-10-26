package com.example.androidmobilebootcampfinalproject.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.androidmobilebootcampfinalproject.R
import com.example.androidmobilebootcampfinalproject.utils.BASE_IMAGE_URL

object ImageBindingAdapter {
    @JvmStatic
    @BindingAdapter("imageUrl")
    fun setUrl(imageView: ImageView, imageUrl : String?){
        Glide.with(imageView.context)
            .load(BASE_IMAGE_URL+"$imageUrl")
            .error(R.drawable.ic_image_not_found)
            .into(imageView)
    }
}
