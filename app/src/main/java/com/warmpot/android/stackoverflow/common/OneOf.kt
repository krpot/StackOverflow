package com.warmpot.android.stackoverflow.common

sealed class OneOf<out T> {
    data class Error(val e: Throwable) : OneOf<Nothing>()
    data class Success<T>(val data: T) : OneOf<T>()
}

fun <T> OneOf<T>.onError(action: (Throwable) -> Unit) {
    if (this is OneOf.Error) {
        action(this.e)
    }
}

fun <T> OneOf<T>.onSuccess(action: (T) -> Unit) {
    if (this is OneOf.Success) {
        action(this.data)
    }
}

suspend fun <T> tryOneOf(action: suspend () -> T): OneOf<T> {
    return try {
        OneOf.Success(action())
    } catch (e: Throwable) {
        e.printStackTrace()
        OneOf.Error(e)
    }
}
