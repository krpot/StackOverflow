package com.warmpot.android.stackoverflow.common

sealed class OneOf<out T> {
    data class Error(val e: Exception) : OneOf<Nothing>()
    data class Success<T>(val data: T) : OneOf<T>()
}
