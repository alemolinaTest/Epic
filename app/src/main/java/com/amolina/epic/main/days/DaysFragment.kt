package com.amolina.epic.main.days

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.amolina.epic.R
import com.amolina.epic.compose.LoadingScreen
import com.amolina.epic.databinding.LayoutComposeContainerBinding
import com.amolina.epic.extensions.getColorCompat
import com.amolina.epic.extensions.setStatusBarAppearance
import com.amolina.epic.main.MainViewModel
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DaysFragment : Fragment(R.layout.layout_compose_container) {

  private val binding by viewBinding(LayoutComposeContainerBinding::bind)

  private val viewModel: MainViewModel by activityViewModels()
  private val navigation by lazy { findNavController() }

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
          content = { _ ->
            // Column(Modifier.padding(paddingValues).fillMaxSize()) {
            daysRows()
            // }
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
      modifier = Modifier.background(Color.White),
      title = {
        Text(
          text = "Days List",
          color = Color.Black,
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
  fun daysRows() {

    val daysCollection = viewModel.allDatesData.observeAsState().value

    if (daysCollection != null) {
      Column(modifier = Modifier.fillMaxSize()) {

        val listState = rememberLazyListState()
        LazyColumn(
          state = listState
        ) {
          items(daysCollection.dates) { day ->
            Row(
              Modifier
                .clickable(
                  onClick = { gotoPhotosScreen(day.date) }
                )
                .padding(15.dp)
                .height(22.dp),

              ) {

              Icon(
                painter = if (!day.downloaded) {
                  painterResource(R.drawable.empty_circle)
                } else {
                  painterResource(R.drawable.check)
                },
                tint = Color.Black,
                contentDescription = "dayDescription"
              )
              Spacer(modifier = Modifier.padding(start = 10.dp, end = 10.dp))
              Text(text = day.date, color = Color.Black)
            }
          }
        }
      }
    } else {
      LoadingScreen()
    }
  }

  private fun gotoPhotosScreen(day: String) {

    val directions = DaysFragmentDirections.toPhotoScreen(
      selectedDay = day
    )
    navigation.navigate(directions)
  }
}