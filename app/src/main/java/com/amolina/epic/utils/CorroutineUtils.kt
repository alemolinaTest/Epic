package com.amolina.epic.utils

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Whether the  Coroutine Job is active
 */
fun CoroutineContext?.inflight(): Boolean = this?.isActive == true

/**
 * Whether the  Coroutine Job is inactive
 */
fun CoroutineContext?.notInflight(): Boolean = !inflight()

/**
 * "Debounces" a function that processes input data and passes it to [destinationFunction] only if there's no new data for at least [waitMs].
 * @see [debounce operators](https://gist.github.com/Terenfear/a84863be501d3399889455f391eeefe5)
 * */
fun <T> debounce(
    waitMs: Long = 333L,
    coroutineScope: CoroutineScope,
    destinationFunction: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        debounceJob?.cancel()
        debounceJob = coroutineScope.launch {
            delay(waitMs)
            destinationFunction(param)
        }
    }
}

fun <T> throttleFirst(
    waitMs: Long = 333L,
    coroutineScope: CoroutineScope,
    destinationFunction: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        if (debounceJob == null) {
            debounceJob = coroutineScope.launch {
                destinationFunction.invoke(param)
                delay(waitMs)
                debounceJob = null
            }
        }
    }
}