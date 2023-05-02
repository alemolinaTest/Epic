package com.amolina.epic.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amolina.epic.common.AsyncStateViewModel
import com.amolina.epic.domain.interactor.FetchDates
import com.amolina.epic.domain.interactor.FetchImageData
import com.amolina.epic.domain.model.DatesCollection
import com.amolina.epic.domain.model.ImagesData
import com.amolina.epic.domain.model.ImagesDataCollection
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

  private val _allDatesData = MutableLiveData<DatesCollection>()
  val allDatesData: LiveData<DatesCollection> = _allDatesData

  private val _allImagesData = MutableLiveData<ImagesDataCollection>()
  val allImagesData: LiveData<ImagesDataCollection> = _allImagesData

  private val _mutableImagesData = mutableListOf<ImagesData>()

  private val _selectedImageUrl = MutableLiveData<String>()
  val selectedImageUrl: LiveData<String> = _selectedImageUrl

  private fun getDates(daysCount: Int) {

    launch {
      val dates = fetchDates.invoke()
      val selectedDates = dates.take(daysCount)
      _allDatesData.value = DatesCollection(dates = selectedDates)
    }
  }

  fun getImageData(selectedDates: String) {
    launch {

      val imagesData = fetchImageData.invoke(selectedDates)
      for (image in imagesData) {
        _mutableImagesData.add(image.copy(url = image.getImageDownloadIdentifier()))
      }
      _allImagesData.value = ImagesDataCollection(imagesData = _mutableImagesData)
    }
  }

  fun setSelectedImageUrl(selectedUrl: String) {
    _selectedImageUrl.value = selectedUrl
  }
}