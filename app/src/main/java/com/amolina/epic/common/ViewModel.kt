package com.amolina.epic.common

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import androidx.lifecycle.viewModelScope
import com.amolina.epic.utils.notInflight
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.lifecycle.ViewModel as BaseViewModel

@HiltViewModel
open class ViewModel @Inject constructor() : BaseViewModel() {

    private var runningJob: Job? = null

    protected fun launch(block: suspend CoroutineScope.() -> Unit) {
        runningJob = viewModelScope.launch { block() }
    }

    protected fun launchIfIdle(block: suspend CoroutineScope.() -> Unit) {
        if (runningJob.notInflight()) launch { block() }
    }
}