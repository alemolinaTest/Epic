package com.amolina.epic.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amolina.epic.common.AsyncStateViewModel
import com.amolina.epic.domain.interactor.FetchDates
import com.amolina.epic.domain.interactor.FetchImageData
import com.amolina.epic.domain.model.ImagesData
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

  private val _allImagesData = MutableLiveData<List<String>>()
  val allImagesData: LiveData<List<String>> = _allImagesData

  private val _mutableImagesData = mutableListOf<String>()

  private fun getDates(daysCount: Int) {

    launchHandlingStates {
      val dates = fetchDates.invoke()
      val selectedDates = dates.getAllDates().take(daysCount)
      _allDatesData.value = selectedDates
      getImagesData(selectedDates)
    }
  }

  private fun getImagesData(selectedDates: List<String>) {
    launchHandlingStates {
      for (date in selectedDates) {
        val imagesData = fetchImageData.invoke(date)
        for (image in imagesData) {
          _mutableImagesData.add(image)
        }
      }
      _allImagesData.value = _mutableImagesData
    }
  }
}