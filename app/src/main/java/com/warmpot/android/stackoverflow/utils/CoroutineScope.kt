package com.warmpot.android.stackoverflow.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch

fun CoroutineScope.singleLaunch(existingJob: Job? = null, apiCall: suspend () -> Unit): Job {
    return this.launch {
        existingJob?.cancelAndJoin()
        apiCall()
    }
}