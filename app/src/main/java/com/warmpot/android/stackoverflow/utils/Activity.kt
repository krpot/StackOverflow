package com.warmpot.android.stackoverflow.utils

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.warmpot.android.stackoverflow.screen.common.viewmodel.ViewModelFactory

inline fun <reified T : ViewModel> FragmentActivity.viewModel(
    factory: ViewModelProvider.Factory = ViewModelFactory
): Lazy<T> = lazy {
    ViewModelProvider(this, factory).get(T::class.java)
}
