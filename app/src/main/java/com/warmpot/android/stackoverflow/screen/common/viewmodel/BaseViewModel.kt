package com.warmpot.android.stackoverflow.screen.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.warmpot.android.stackoverflow.utils.singleLaunch
import kotlinx.coroutines.Job

abstract class BaseViewModel : ViewModel() {
    private var launchJob: Job? = null

    fun singleLaunch(apiCall: suspend () -> Unit) {
        launchJob = viewModelScope.singleLaunch(launchJob) {
            apiCall()
        }
    }
}


