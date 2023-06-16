package com.amolina.epic.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amolina.epic.common.AsyncStateViewModel
import com.amolina.epic.domain.interactor.FetchDates
import com.amolina.epic.domain.interactor.FetchImageData
import com.amolina.epic.domain.model.DatesCollection
import com.amolina.epic.domain.model.ImagesData
import com.amolina.epic.domain.model.ImagesDataCollection
import com.amolina.epic.main.MainViewModel.ResultsState.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel class MainViewModel @Inject constructor(
  private val fetchDates: FetchDates,
  private val fetchImageData: FetchImageData,
): AsyncStateViewModel() {
  private val _allDatesData = MutableLiveData<DatesCollection>()
  val allDatesData: LiveData<DatesCollection> = _allDatesData
  private val _allImagesData = MutableLiveData<ImagesDataCollection>()
  val allImagesData: LiveData<ImagesDataCollection> = _allImagesData
  private val _mutableImagesData = mutableListOf<ImagesData>()
  private val _selectedImageUrl = MutableLiveData<String>()
  val selectedImageUrl: LiveData<String> = _selectedImageUrl
  private val _resultsState = MutableLiveData<ResultsState>()
  val resultsState: LiveData<ResultsState>
    get() = _resultsState

  companion object {
    const val DAYS_COUNT = 10
  }

  init {
    getDates(daysCount = DAYS_COUNT)
  }

  fun getDates(daysCount: Int) {
    launchHandlingErrors {
      _resultsState.value = Loading
      val dates = fetchDates()
      dates?.let {
        val selectedDates = it.take(daysCount)
        _allDatesData.value = DatesCollection(dates = selectedDates)
        _resultsState.value = Complete
      }
    }
  }

  fun getImageData(selectedDates: String) {
    launchHandlingErrors {
      _resultsState.value = Loading
      val imagesData = fetchImageData(selectedDates)
      imagesData?.let { images ->
        images.mapTo(_mutableImagesData) { it.copy(url = it.getImageDownloadIdentifier()) }
        _allImagesData.value = ImagesDataCollection(imagesData = _mutableImagesData)
        _resultsState.value = Complete
      }
    }
  }

  fun refreshAllData() {
    launchHandlingErrors {
      _resultsState.value = Loading
      val dates = fetchDates.refreshData()
      dates?.let {
        val selectedDates = it.take(DAYS_COUNT)
        _allDatesData.value = DatesCollection(dates = selectedDates)
        _resultsState.value = Complete
      }
    }
  }

  fun setSelectedImageUrl(selectedUrl: String) {
    _selectedImageUrl.value = selectedUrl
  }

  fun setErrorState() {
    _resultsState.value = Error
  }

  sealed class ResultsState {
    object Error: ResultsState()
    object Loading: ResultsState()
    object Complete: ResultsState()
  }
}