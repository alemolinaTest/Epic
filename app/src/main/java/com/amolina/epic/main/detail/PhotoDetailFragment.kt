package com.amolina.epic.main.detail

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amolina.epic.R
import com.amolina.epic.databinding.LayoutComposeContainerBinding
import com.amolina.epic.extensions.getColorCompat
import com.amolina.epic.extensions.setStatusBarAppearance
import com.amolina.epic.main.MainViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint class PhotoDetailFragment: Fragment(R.layout.layout_compose_container) {
  private val binding by viewBinding(LayoutComposeContainerBinding::bind)
  private val viewModel: MainViewModel by activityViewModels()
  private val navigation by lazy { findNavController() }
  private val screenArgs: PhotoDetailFragmentArgs by navArgs()

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

    viewModel.setSelectedImageUrl(screenArgs.selectedUrl)

    (view as ComposeView).apply {
      setContent {
        Scaffold(modifier = Modifier
          .background(Color.Red)
          .fillMaxSize()
          .padding(1.dp),
                 topBar = { TopBar() },
                 content = {
                   photosDetail()
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
    TopAppBar(modifier = Modifier.background(Color.White),
              title = {
                Text(
                  text = "Photos Detail",
                  color = Color.Green,
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
  fun photosDetail() {
    val selectedPhotoUrl = viewModel.selectedImageUrl.observeAsState().value

    if (selectedPhotoUrl != null) {
      Column(modifier = Modifier.fillMaxSize()) {
        val listState = rememberLazyListState()
        LazyColumn(state = listState) {
          item {
            ImageItem(selectedPhotoUrl)
          }
        }
      }
    }
  }

  @Composable
  fun loadPicture(
    url: String,
    placeholder: Painter? = null,
  ): Painter? {
    var state by remember {
      mutableStateOf(placeholder)
    }
    val options: RequestOptions = RequestOptions()
      .autoClone()
      .diskCacheStrategy(DiskCacheStrategy.ALL)
    val context = LocalContext.current
    val result = object: CustomTarget<Bitmap>() {
      override fun onLoadCleared(p: Drawable?) {
        state = placeholder
      }

      override fun onResourceReady(
        resource: Bitmap,
        transition: Transition<in Bitmap>?,
      ) {
        state = BitmapPainter(resource.asImageBitmap())
      }
    }
    try {
      Glide
        .with(context)
        .asBitmap()
        .load(url)
        .apply(options)
        .into(result)
    } catch (e: Exception) { // Can't use LocalContext in Compose Preview
    }
    return state
  }

  @Composable
  fun ImageItem(url: String) {
    val painter = loadPicture(url = url,
                              placeholder = painterResource(id = R.drawable.empty_circle))
    if (painter != null) {
      ZoomableImage(painter)
    }
  }

  @Composable
  fun ZoomableImage(painter: Painter) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Box(Modifier.size(600.dp)) {
      Image(painter = painter,
            contentDescription = "A Content description",
            modifier = Modifier
              .align(Alignment.Center)
              .graphicsLayer(scaleX = scale,
                             scaleY = scale,
                             translationX = if (scale > 1f) offset.x else 0f,
                             translationY = if (scale > 1f) offset.y else 0f)
              .pointerInput(Unit) {
                detectTransformGestures(onGesture = { _, pan: Offset, zoom: Float, _ ->
                  offset += pan
                  scale = (scale * zoom).coerceIn(0.5f,
                                                  4f)
                })
              })
    }
  }
}