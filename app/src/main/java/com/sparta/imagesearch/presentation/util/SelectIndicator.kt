package com.sparta.imagesearch.presentation.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.sparta.imagesearch.R
import com.sparta.imagesearch.presentation.theme.Padding
import com.sparta.imagesearch.presentation.theme.scheme

const val SELECT_INDICATOR_SCALE = 1.5f
@Composable
fun SelectIndicator(
    modifier: Modifier = Modifier,
    alpha: Float = 1.0f,
    color: Color = MaterialTheme.scheme.onSurface
) {
    Image(
        modifier = modifier
            .padding(Padding.small)
            .scale(SELECT_INDICATOR_SCALE)
            .alpha(alpha),
        painter = painterResource(id = R.drawable.icon_dot),
        colorFilter = ColorFilter.tint(color),
        contentDescription = ""
    )
}