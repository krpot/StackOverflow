package com.warmpot.android.stackoverflow.domain.entity


abstract class UseCaseResult<T> {
    abstract val hasMore: Boolean
    abstract val data: T
    abstract val quotaMax: Int
    abstract val quotaRemaining: Int
}
