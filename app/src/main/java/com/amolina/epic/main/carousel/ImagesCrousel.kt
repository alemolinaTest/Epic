package com.amolina.epic.main.carousel

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.amolina.epic.R
import com.amolina.epic.R.drawable
import com.amolina.epic.main.MainViewModel
import com.bumptech.glide.request.RequestOptions
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint class ImagesCrousel: Fragment(R.layout.layout_compose_container) {
  private val viewModel: MainViewModel by activityViewModels()
  private val navigation by lazy { findNavController() }

  @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
  override fun onViewCreated(
    view: View,
    savedInstanceState: Bundle?,
  ) {
    super.onViewCreated(view,
                        savedInstanceState)

    (view as ComposeView).apply {
      setContent {
        Scaffold(modifier = Modifier
          .background(Color.Red)
          .fillMaxSize()
          .padding(1.dp),
                 topBar = { TopBar() },
                 content = {
                   val imagesCollection = viewModel.allImagesData.observeAsState().value
                   imagesCollection?.imagesData?.let { it1 ->
                     carouselContent(modifier = Modifier,
                                     listImageUrl = it1.map { it.url })
                   }
                 })
      }
    }
  }

  @Composable
  private fun TopBar() {
    TopAppBar(modifier = Modifier.background(Color.White),
              title = {
                Text(
                  text = "Images List Slider",
                  color = Color.Black,
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

  @OptIn(ExperimentalFoundationApi::class)
  @Composable
  fun carouselContent(
    modifier: Modifier = Modifier,
    listImageUrl: List<String> = listOf(),
  ) {
    val state = rememberPagerState()
    HorizontalPager(state = state,
                    pageCount = listImageUrl.size,
                    modifier = modifier) { pagerScope ->
      Box(contentAlignment = Alignment.BottomCenter) {
        carouselImage(imageUrl = listImageUrl[pagerScope])
      }
    }
  }

  @Composable
  fun carouselImage(imageUrl: String) {
    val requestOptions = RequestOptions()
      .override(1000,
                1000)
      .optionalCenterInside()

    GlideImage(modifier = Modifier
      .clickable(onClick = { })
      .padding(top = 2.dp,
               bottom = 2.dp),
               imageModel = { imageUrl },
               requestOptions = { requestOptions },
               imageOptions = ImageOptions(contentScale = ContentScale.Fit,
                                           alignment = Alignment.Center,
                                           contentDescription = "slider image",
                                           colorFilter = null,
                                           alpha = 1f),
               previewPlaceholder = drawable.empty_circle, // shows a progress indicator when loading an image.
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
