package com.warmpot.android.stackoverflow.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.warmpot.android.stackoverflow.screen.common.viewmodel.ViewModelFactory

inline fun <reified T : ViewModel> ViewModelStoreOwner.viewModel(
    factory: ViewModelProvider.Factory = ViewModelFactory,
    key: String? = null
): Lazy<T> = lazy {
    if (key == null)
        ViewModelProvider(this, factory).get(T::class.java)
    else
        ViewModelProvider(this, factory).get(key, T::class.java)
}

inline fun <reified T : ViewModel> Fragment.activityViewModel(
    factory: ViewModelProvider.Factory = ViewModelFactory
): Lazy<T> = lazy {
    ViewModelProvider(requireActivity(), factory).get(T::class.java)
}
