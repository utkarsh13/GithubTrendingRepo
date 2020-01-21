package com.example.gojekassignment.utils

import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.gojekassignment.R


@BindingAdapter("imageUrl")
fun setImageUrl(imageView: ImageView, url: String) {
    val unwrappedDrawable = AppCompatResources.getDrawable(imageView.context, R.drawable.circle)
    val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
    DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(imageView.context, R.color.shimmer_background))

    Glide
        .with(imageView.context)
        .load(url)
        .apply(RequestOptions().circleCrop())
        .apply(RequestOptions().placeholder(wrappedDrawable))
        .into(imageView)
}

@BindingAdapter("backgroundColor")
fun setBackgroundColor(view: View, color: String?) {
    color?.let {
        val unwrappedDrawable = AppCompatResources.getDrawable(view.context, R.drawable.circle)
        val wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable!!)
        DrawableCompat.setTint(wrappedDrawable, Color.parseColor(it))
        view.setBackgroundDrawable(wrappedDrawable)
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