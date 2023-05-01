package com.amolina.epic.compose

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun LoadingScreen() {
    Column(modifier = Modifier.padding(horizontal = 2.dp)) {
        repeat(6) {
            ShimmerSpacer()
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}


@Composable
fun ShimmerSpacer(
    modifier: Modifier = Modifier,
){
    val colors = listOf(
        Color.Gray,
        Color.Black,
        Color.White
    )


    val transition = rememberInfiniteTransition()

    val shimmerWidthPercentage = 2.5f

    BoxWithConstraints {
        val spaceMaxWidth = with(LocalDensity.current) { maxWidth.toPx() }
        val spaceMaxHeight = with(LocalDensity.current) { maxHeight.toPx() }

        val translateAnim = transition.animateFloat(
            initialValue = 0f,
            targetValue = spaceMaxWidth * (1 + shimmerWidthPercentage),
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1400,
                    easing = FastOutSlowInEasing
                ),
                repeatMode = RepeatMode.Restart
            )
        )

        val brush = Brush.linearGradient(
            colors,
            start = Offset(translateAnim.value - (spaceMaxWidth * shimmerWidthPercentage),spaceMaxHeight),
            end = Offset(translateAnim.value,spaceMaxHeight)
        )


        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(brush = brush, shape = RoundedCornerShape(4.dp))
        )

    }
}