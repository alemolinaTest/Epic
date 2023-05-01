package com.amolina.epic.main.photo

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.amolina.epic.R
import com.amolina.epic.compose.LoadingScreen
import com.amolina.epic.databinding.LayoutComposeContainerBinding
import com.amolina.epic.extensions.getColorCompat
import com.amolina.epic.extensions.setStatusBarAppearance
import com.amolina.epic.main.MainViewModel
import com.skydoves.landscapist.glide.GlideImage
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoFragment : Fragment(R.layout.layout_compose_container) {

  private val binding by viewBinding(LayoutComposeContainerBinding::bind)

  private val viewModel: MainViewModel by activityViewModels()
  private val navigation by lazy { findNavController() }

  companion object {
    private const val THUMBNAIL_DIMENSION = 50
    private val THUMBNAIL_SIZE = Size(THUMBNAIL_DIMENSION.toFloat(), THUMBNAIL_DIMENSION.toFloat())
  }

  @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    (view as ComposeView).apply {
      setContent {
        Scaffold(
          modifier = Modifier
            .background(Color.Red)
            .fillMaxSize()
            .padding(1.dp),
          topBar = { TopBar() },
          content = {
            photosRows()
          }
        )
      }
    }
  }

  override fun onResume() {
    super.onResume()
    setLightStatusBar()
  }

  private fun setLightStatusBar() {
    with(requireActivity()) {
      setStatusBarAppearance(true, requireView())
      requireActivity().window.statusBarColor = resources.getColorCompat(R.color.white)
    }
  }

  @Composable
  private fun TopBar() {
    TopAppBar(
      modifier = Modifier.background(Color.Yellow),
      title = {
        Text(
          text = "Photos List",
          color = Color.Blue,
        )
      },
      backgroundColor = Color.White,
      navigationIcon = {
        IconButton(onClick = { requireActivity().onBackPressed() }) {
          Icon(Filled.ArrowBack, "")
        }
      },
      actions = {

      },
      elevation = 2.dp
    )
  }

  @Composable
  fun photosRows() {

    val imagesCollection = viewModel.allImagesData.observeAsState().value
    if (imagesCollection != null) {
      Column(modifier = Modifier.fillMaxSize()) {

        val listState = rememberLazyListState()
        LazyColumn(
          state = listState
        ) {
          items(imagesCollection.imagesData) { image ->
            Row(
              Modifier
                .padding(15.dp)
                .height(22.dp),
            ) {

              Icon(
                painter = painterResource(R.drawable.empty_circle),
                tint = Color.Black,
                contentDescription = "dayDescription"
              )
              Spacer(modifier = Modifier.padding(start = 10.dp, end = 10.dp))
              Text(text = image.url, color = Color.Black)
            }

//            GlideImage(
//              imageModel = image.url,
//              // shows a progress indicator when loading an image.
//              loading = {
//                ConstraintLayout(
//                  modifier = Modifier.fillMaxSize()
//                ) {
//                  val indicator = createRef()
//                  CircularProgressIndicator(
//                    modifier = Modifier.constrainAs(indicator) {
//                      top.linkTo(parent.top)
//                      bottom.linkTo(parent.bottom)
//                      start.linkTo(parent.start)
//                      end.linkTo(parent.end)
//                    }
//                  )
//                }
//              },
//              // shows an error text message when request failed.
//              failure = {
//                Text(text = "image request failed.")
//              })
          }
        }
      }
    } else {
      LoadingScreen()
    }
  }
}