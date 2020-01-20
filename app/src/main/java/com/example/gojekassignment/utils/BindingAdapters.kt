package com.example.gojekassignment.utils

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.gojekassignment.R

@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String) {
    imageView.setBackgroundColor(
        ContextCompat.getColor(
            imageView.context, R.color.placeholderColor

        )
    )
    Glide
        .with(imageView.context)
        .load(url)
        .apply(RequestOptions().circleCrop())
        .into(imageView)
}

@BindingAdapter("backgroundColor")
fun setBackgroundColor(view: View, color: String?) {
    color?.let {
        view.setBackgroundColor(Color.parseColor(it))
    }
}

@BindingAdapter("setText")
fun setText(tv: TextView, count: Int?) {
    if (count != null) {
        tv.text = count.toString()
    } else {
        tv.visibility = View.GONE
    }
}