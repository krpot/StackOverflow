package com.warmpot.android.stackoverflow.screen.common

import kotlinx.coroutines.Job

fun Job?.isActuallyActive(): Boolean {
    if (this == null) return false
    return this.isActive
}
