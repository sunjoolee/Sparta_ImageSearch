package com.sparta.imagesearch.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import com.skydoves.landscapist.glide.GlideImageState
import com.sparta.imagesearch.R
import com.sparta.imagesearch.domain.FolderColor
import com.sparta.imagesearch.domain.Item
import com.sparta.imagesearch.presentation.theme.Padding
import com.sparta.imagesearch.presentation.theme.scheme
import com.sparta.imagesearch.presentation.util.ShimmerBrush

val ITEM_HEART_SCALE = 1.2f
val ITEM_HEART_ICON_SCALE = 1.5f
val ITEM_IMAGE_SHIMMER_SIZE = 160.dp

@Composable
fun ImageSearchItem(
    modifier: Modifier = Modifier,
    item: Item,
    heartColorHex: String = FolderColor.NO_COLOR.colorHex,
    onHeartClick: (item: Item) -> Unit = {},
    onHeartLongClick: (item: Item) -> Unit = {}
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.scheme.surface,
            contentColor = MaterialTheme.scheme.onSurface
        )
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = modifier.fillMaxWidth()
            ) {
                ItemImage(
                    modifier = modifier
                        .align(Alignment.Center)
                        .background(MaterialTheme.scheme.disabled),
                    imageUrl = item.imageUrl
                )
                ItemHeart(
                    modifier = modifier
                        .align(Alignment.TopEnd)
                        .padding(Padding.medium)
                        .scale(ITEM_HEART_SCALE),
                    colorHex = heartColorHex,
                    onHeartClick = { onHeartClick(item) },
                    onHeartLongClick = { onHeartLongClick(item) }
                )
            }
            Column(
                modifier = modifier.padding(Padding.medium)
            ) {
                Text(
                    text = item.source,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    color = MaterialTheme.scheme.disabled,
                    text = item.time,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}


@Composable
fun ItemImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    var state by remember { mutableStateOf<GlideImageState>(GlideImageState.None) }

    if (state is GlideImageState.None || state is GlideImageState.Loading) {
        Spacer(
            modifier = modifier
                .fillMaxWidth()
                .size(ITEM_IMAGE_SHIMMER_SIZE)
                .background(brush = ShimmerBrush())
        )
    }
    GlideImage(
        modifier = modifier,
        imageModel = { imageUrl },
        imageOptions = ImageOptions(
            contentScale = ContentScale.FillWidth
        ),
        onImageStateChanged = {
            state = it
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemHeart(
    modifier: Modifier = Modifier,
    colorHex: String = FolderColor.NO_COLOR.colorHex,
    onHeartClick: () -> Unit,
    onHeartLongClick: () -> Unit = {}
) {
    val heartColor = animateColorAsState(
        targetValue = Color(android.graphics.Color.parseColor(colorHex)),
        label = "heart_color"
    )

    Image(
        painter = painterResource(id = R.drawable.icon_heart),
        colorFilter = ColorFilter.tint(color = heartColor.value),
        contentDescription = "",
        modifier = modifier
            .scale(ITEM_HEART_ICON_SCALE)
            .combinedClickable(
                onClick = onHeartClick,
                onLongClick = onHeartLongClick
            )
    )
}
