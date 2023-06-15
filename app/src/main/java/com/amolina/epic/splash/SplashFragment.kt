package com.amolina.epic.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.fragment.findNavController
import com.amolina.epic.R
import com.amolina.epic.extensions.getColorCompat
import com.amolina.epic.extensions.setStatusBarAppearance
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint class SplashFragment: Fragment(R.layout.layout_compose_container) {
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
        SplashScreen(modifier = Modifier.padding(2.dp),
                     valid = true,
                     onStart = {},
                     onSplashEndedInvalid = { gotoDayScreen() },
                     onSplashEndedValid = { gotoDayScreen() })
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
  fun SplashScreen(
    modifier: Modifier = Modifier,
    valid: Boolean?,
    onStart: () -> Unit,
    onSplashEndedValid: () -> Unit,
    onSplashEndedInvalid: () -> Unit,
  ) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val color = remember { Animatable(Color.Black) }
    val currentValid = rememberUpdatedState(newValue = valid)

    LaunchedEffect(key1 = null) {
      delay(2000)
      if (currentValid.value == true) onSplashEndedValid()
      else onSplashEndedInvalid()
    }

    LaunchedEffect(key1 = valid) {
      valid?.let { valid ->
        val animateToColor = if (valid) Color.Green else Color.Red
        color.animateTo(animateToColor,
                        animationSpec = tween(500))
      }
    }
    Column(modifier = modifier.fillMaxSize(),
           verticalArrangement = Arrangement.Center,
           horizontalAlignment = Alignment.CenterHorizontally) {
      Image(
        painter = painterResource(id = R.drawable.ic_launcher_foreground),
        contentDescription = null,
        colorFilter = ColorFilter.tint(color.value),
        modifier = Modifier.size(100.dp),
      )
    }

    DisposableEffect(lifecycleOwner) {
      val observer = LifecycleEventObserver { _, event ->
        if (event == Lifecycle.Event.ON_START) {
          onStart()
        }
      }
      lifecycleOwner.lifecycle.addObserver(observer)

      onDispose {
        lifecycleOwner.lifecycle.removeObserver(observer)
      }
    }
  }

  private fun gotoDayScreen() {
    val directions = SplashFragmentDirections.toDayScreen()
    navigation.navigate(directions)
  }
}