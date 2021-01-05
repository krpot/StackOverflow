package com.warmpot.android.stackoverflow.screen.utils

fun <T> nonSyncLazy(initializer: () -> T): Lazy<T> =
    lazy(LazyThreadSafetyMode.NONE, initializer)