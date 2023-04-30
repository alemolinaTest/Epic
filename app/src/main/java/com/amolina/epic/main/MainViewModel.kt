package com.amolina.epic.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amolina.epic.common.AsyncStateViewModel
import com.amolina.epic.domain.interactor.FetchDates
import com.amolina.epic.domain.interactor.FetchImageData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
  private val fetchDates: FetchDates,
  private val fetchImageData: FetchImageData
) : AsyncStateViewModel() {

  companion object {
    const val DAYS_COUNT = 10
  }

  init {
    getDates(daysCount = DAYS_COUNT)
  }

  private val _allDatesData = MutableLiveData<List<String>>()
  val allDatesData: LiveData<List<String>> = _allDatesData

  private fun getDates(daysCount: Int) {

    launchHandlingStates {
      val dates = fetchDates.invoke()
      val selectedDates = dates.getAllDates().take(daysCount)
      _allDatesData.value = selectedDates
      for (date in selectedDates) {
        val imageData = fetchImageData.invoke(date)
      }
    }
  }
}