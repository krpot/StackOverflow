package com.warmpot.android.stackoverflow.utils

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

inline fun <reified T : ViewModel> FragmentActivity.viewModel(): Lazy<T> = lazy {
    ViewModelProvider(this).get(T::class.java)
}
