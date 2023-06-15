package com.amolina.epic.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

inline fun <T> LiveData<Event<T>>.observeEvent(
    owner: LifecycleOwner,
    crossinline onEventUnhandledContent: (T) -> Unit,
) {
  observe(owner,
          {
            it
              ?.getContentIfNotHandled()
              ?.let(onEventUnhandledContent)
          })
}