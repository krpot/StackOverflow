package com.warmpot.android.stackoverflow.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.warmpot.android.stackoverflow.R
import com.warmpot.android.stackoverflow.screen.common.image.ImageLoader

fun ImageView.load(url: String, @DrawableRes placeholderId: Int = R.mipmap.ic_launcher) {
    ImageLoader().loadImage(view = this, url = url, placeholderId = placeholderId)
}

fun ImageView.circle(url: String, @DrawableRes placeholderId: Int = R.mipmap.ic_launcher) {
    ImageLoader().loadCircleImage(view = this, url = url, placeholderId = placeholderId)
}
