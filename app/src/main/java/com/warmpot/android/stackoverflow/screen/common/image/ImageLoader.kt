package com.warmpot.android.stackoverflow.screen.common.image

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide

class ImageLoader {

    fun loadImage(view: ImageView, url: String, @DrawableRes placeholderId: Int) {
        Glide
            .with(view)
            .load(url)
            .centerCrop()
            .placeholder(placeholderId)
            .into(view);
    }

    fun loadCircleImage(view: ImageView, url: String, @DrawableRes placeholderId: Int) {
        Glide
            .with(view)
            .load(url)
            .circleCrop()
            .placeholder(placeholderId)
            .into(view);
    }
}
