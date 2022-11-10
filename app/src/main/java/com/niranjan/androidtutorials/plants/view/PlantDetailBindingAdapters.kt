package com.niranjan.androidtutorials.plants.view

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.Coil
import coil.ImageLoader
import coil.load
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Random

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        view.load(imageUrl)
        Coil.setImageLoader(ImageLoader(view.context))
    }
}

@BindingAdapter("isGone")
fun bindIsGone(view: FloatingActionButton, isGone: Boolean?) {
    if (isGone == null || isGone) {
        view.hide()
    } else {
        view.show()
    }
}


@BindingAdapter("randomNumber")
fun TextView.setRandomNumber(isRandom: Boolean){
    text = if (isRandom) Random().nextInt().toString() else "0"
}