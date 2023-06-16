package com.amolina.epic.main.carousel

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
    val coroutineScope = rememberCoroutineScope()
    HorizontalPager(state = state,
                    pageCount = listImageUrl.size,
                    modifier = modifier) { pagerScope ->
      Column(modifier = Modifier.fillMaxSize(),
             horizontalAlignment = Alignment.CenterHorizontally) {
        Box(contentAlignment = Alignment.BottomCenter) {
          carouselImage(imageUrl = listImageUrl[pagerScope])
        }
        Box(contentAlignment = Alignment.BottomCenter) {
          sliderButtons(pagerState = state,
                        coroutineScope = coroutineScope,
                        maxItemsValue = listImageUrl.size)
        }
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

  @OptIn(ExperimentalFoundationApi::class)
  @Composable
  fun sliderButtons(
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    maxItemsValue: Int,
  ) {
    Row(modifier = Modifier.padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
      val prevButtonVisible = remember {
        derivedStateOf {
          pagerState.currentPage > 0
        }
      }
      val nextButtonVisible = remember {
        derivedStateOf {
          pagerState.currentPage < maxItemsValue
        }
      }

      Button(
        enabled = prevButtonVisible.value,
        onClick = {
          val prevPageIndex = pagerState.currentPage - 1
          coroutineScope.launch { pagerState.animateScrollToPage(prevPageIndex) }
        },
      ) {
        Text(text = "Prev")
      }

      Button(
        enabled = nextButtonVisible.value,
        onClick = {
          val nextPageIndex = pagerState.currentPage + 1
          coroutineScope.launch { pagerState.animateScrollToPage(nextPageIndex) }
        },
      ) {
        Text(text = "Next")
      }
    }
  }
}
