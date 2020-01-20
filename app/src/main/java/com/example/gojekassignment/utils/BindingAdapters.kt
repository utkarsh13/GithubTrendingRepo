package com.example.gojekassignment.utils

import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.gojekassignment.R

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String) {
    imageView.setBackgroundColor(
        ContextCompat.getColor(
            imageView.context, R.color.placeholderColor

        )
    )
    Glide.with(imageView.context).load(url).into(imageView)
}