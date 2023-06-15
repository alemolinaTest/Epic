package com.amolina.epic.main.photo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amolina.epic.R
import com.amolina.epic.compose.LoadingScreen
import com.amolina.epic.domain.model.ImagesDataCollection
import com.amolina.epic.extensions.getColorCompat
import com.amolina.epic.extensions.setStatusBarAppearance
import com.amolina.epic.main.MainViewModel
import com.bumptech.glide.request.RequestOptions
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint class PhotoFragment: Fragment(R.layout.layout_compose_container) {
  private val viewModel: MainViewModel by activityViewModels()
  private val navigation by lazy { findNavController() }
  private val screenArgs: PhotoFragmentArgs by navArgs()

  companion object {
    private const val THUMBNAIL_DIMENSION = 50
    private val THUMBNAIL_SIZE = Size(THUMBNAIL_DIMENSION.toFloat(),
                                      THUMBNAIL_DIMENSION.toFloat())
  }

  @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?,
  ) {
    super.onViewCreated(view,
                        savedInstanceState)

    viewModel.getImageData(screenArgs.selectedDay)

    (view as ComposeView).apply {
      setContent {
        Scaffold(modifier = Modifier
          .background(Color.Red)
          .fillMaxSize()
          .padding(1.dp),
                 topBar = { TopBar() },
                 content = {
                   val imagesCollection = viewModel.allImagesData.observeAsState().value
                   photosRows(imagesCollection)
                 })
      }
    }
  }

  override fun onResume() {
    super.onResume()
    setLightStatusBar()
  }

  private fun setLightStatusBar() {
    with(requireActivity()) {
      setStatusBarAppearance(true,
                             requireView())
      requireActivity().window.statusBarColor = resources.getColorCompat(R.color.white)
    }
  }

  @Composable
  private fun TopBar() {
    TopAppBar(modifier = Modifier.background(Color.Yellow),
              title = {
                Text(
                  text = "Photos List",
                  color = Color.Blue,
                )
              },
              backgroundColor = Color.White,
              navigationIcon = {
                IconButton(onClick = { requireActivity().onBackPressed() }) {
                  Icon(Filled.ArrowBack,
                       "")
                }
              },
              actions = {},
              elevation = 2.dp)
  }

  @Composable
  fun photosRows(imagesCollection: ImagesDataCollection?) {
    if (imagesCollection != null) {
      Column(modifier = Modifier.fillMaxSize()) {
        val requestOptions = RequestOptions()
          .override(500,
                    500)
          .optionalCenterInside()

        LazyVerticalGrid(columns = GridCells.Fixed(2),
                         verticalArrangement = Arrangement.spacedBy(16.dp),
                         horizontalArrangement = Arrangement.spacedBy(16.dp)) {
          items(imagesCollection.imagesData) { image ->
            GlideImage(modifier = Modifier
              .clickable(onClick = { gotoPhotoDetailScreen(image.url) })
              .padding(top = 2.dp,
                       bottom = 2.dp),
                       imageModel = { image.url },
                       requestOptions = { requestOptions },
                       imageOptions = ImageOptions(contentScale = ContentScale.Fit,
                                                   alignment = Alignment.Center,
                                                   contentDescription = "main image",
                                                   colorFilter = null,
                                                   alpha = 1f),
                       previewPlaceholder = R.drawable.empty_circle, // shows a progress indicator when loading an image.
                       loading = {
                         ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                           val indicator = createRef()
                           CircularProgressIndicator(modifier = Modifier.constrainAs(indicator) {
                             top.linkTo(parent.top)
                             bottom.linkTo(parent.bottom)
                             start.linkTo(parent.start)
                             end.linkTo(parent.end)
                           })
                         }
                       }, // shows an error text message when request failed.
                       failure = {
                         Text(text = "image request failed.")
                       })
          }
        }
      }
    } else {
      LoadingScreen()
    }
  }

  private fun gotoPhotoDetailScreen(url: String) {
    val directions = PhotoFragmentDirections.toPhotoDetail(selectedUrl = url)
    navigation.navigate(directions)
  }
}