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
import com.sparta.imagesearch.data.source.local.folder.FolderColor
import com.sparta.imagesearch.data.source.local.folder.FolderId
import com.sparta.imagesearch.domain.Item
import com.sparta.imagesearch.util.ShimmerBrush

@Composable
fun ImageSearchItem(
    modifier: Modifier = Modifier,
    item: Item,
    onHeartClick: (item: Item) -> Unit = {},
    onHeartLongClick: (item: Item) -> Unit = {}
) {
    val folderColor = remember {
        if (item.folderId == FolderId.NO_FOLDER.id) FolderColor.color0.colorHex
        else FolderColor.color1.colorHex
    }

    Card(
        modifier = modifier
    ) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = modifier.fillMaxWidth()
            ) {
                ItemImage(
                    modifier = modifier.align(Alignment.Center),
                    imageUrl = item.imageUrl
                )
                ItemHeart(
                    modifier = modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .scale(1.2f),
                    folderColor = folderColor,
                    onHeartClick = { onHeartClick(item) },
                    onHeartLongClick = { onHeartLongClick(item) })

            }
            Column(
                modifier = modifier.padding(8.dp)
            ) {
                Text(text = item.time)
                Text(text = item.source)
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
                .size(160.dp)
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
    folderColor: String = FolderColor.color0.colorHex,
    onHeartClick: () -> Unit,
    onHeartLongClick: () -> Unit = {}
) {
    val heartColor = animateColorAsState(
        targetValue = Color(android.graphics.Color.parseColor(folderColor)),
        label = "heart_color"
    )

    Image(
        painter = painterResource(id = R.drawable.icon_heart),
        colorFilter = ColorFilter.tint(color = heartColor.value),
        contentDescription = "",
        modifier = modifier
            .scale(1.5f)
            .combinedClickable(
                onClick = onHeartClick,
                onLongClick = onHeartLongClick
            )
    )
}
