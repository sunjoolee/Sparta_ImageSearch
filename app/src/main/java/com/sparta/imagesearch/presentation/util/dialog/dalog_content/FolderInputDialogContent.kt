package com.sparta.imagesearch.presentation.util.dialog.dalog_content

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sparta.imagesearch.R
import com.sparta.imagesearch.presentation.theme.Padding
import com.sparta.imagesearch.presentation.util.SelectIndicator
import com.sparta.imagesearch.presentation.util.hexToColor

val FOLDER_NAME_INPUT_TEXT_FIELD_HEIGHT = 48.dp
val FOLDER_COLOR_INPUT_ICON_SCALE = 1.2f
val FOLDER_COLOR_ITEM_SIZE = 28.dp

@Composable
fun FolderInputDialogContent(
    folderName: String,
    onFolderNameChange: (String) -> Unit,
    colorHexes: List<String>,
    selectedColorHex: String,
    onColorHexSelect: (String) -> Unit
) {
    Column {
        FolderNameInput(
            value = folderName,
            onValueChange = onFolderNameChange
        )
        Spacer(modifier = Modifier.padding(top = Padding.default))
        FolderColorInput(
            colorHexes = colorHexes,
            selectedColorHex = selectedColorHex,
            onColorSelect = onColorHexSelect
        )
    }
}


@Composable
fun FolderNameInput(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = modifier.padding(end = Padding.medium),
            text = stringResource(R.string.add_folder_name)
        )
        TextField(
            modifier = modifier.height(FOLDER_NAME_INPUT_TEXT_FIELD_HEIGHT),
            value = value,
            singleLine = true,
            onValueChange = onValueChange,
            placeholder = {
                Text(text = stringResource(R.string.add_folder_name_hint))
            }
        )
    }
}

@Composable
fun FolderColorInput(
    modifier: Modifier = Modifier,
    colorHexes: List<String>,
    selectedColorHex: String,
    onColorSelect: (String) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = modifier.padding(end = Padding.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = modifier.scale(FOLDER_COLOR_INPUT_ICON_SCALE),
                painter = painterResource(id = R.drawable.icon_folder),
                colorFilter = ColorFilter.tint(selectedColorHex.hexToColor()),
                contentDescription = ""
            )
            Text(
                modifier = modifier,
                text = stringResource(R.string.add_folder_color)
            )
        }
        FolderColorList(
            modifier = modifier.fillMaxWidth(),
            colorHexes = colorHexes,
            selectedColorHex = selectedColorHex,
            onColorSelect = onColorSelect
        )
    }
}

@Composable
fun FolderColorList(
    modifier: Modifier = Modifier,
    colorHexes: List<String>,
    selectedColorHex: String,
    onColorSelect: (String) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        colorHexes.forEach { colorHex ->
            FolderColorItem(
                color = colorHex.hexToColor(),
                colorSelected = (colorHex == selectedColorHex),
                onColorSelect = { onColorSelect(colorHex) }
            )
        }
    }
}

@Composable
fun FolderColorItem(
    modifier: Modifier = Modifier,
    color: Color,
    colorSelected: Boolean,
    onColorSelect: () -> Unit
) {
    val indicatorAlpha = animateFloatAsState(
        targetValue = if (colorSelected) 1.0f else 0.0f, label = "dot_alpha"
    )

    Column(
        modifier = modifier.padding(end = Padding.medium),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(
            modifier = Modifier
                .size(FOLDER_COLOR_ITEM_SIZE)
                .drawBehind { drawCircle(color = color) }
                .clickable { onColorSelect() }
        )
        SelectIndicator(
            alpha = indicatorAlpha.value
        )
    }
}
