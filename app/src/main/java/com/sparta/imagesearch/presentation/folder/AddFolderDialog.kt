package com.sparta.imagesearch.presentation.folder

import android.graphics.Color.parseColor
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sparta.imagesearch.R
import com.sparta.imagesearch.data.source.local.folder.FolderColor

@Composable
fun AddFolderDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    addFolder: (String, String) -> Unit
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var colorHex by remember { mutableStateOf(FolderColor.color1.colorHex) }

    val isNameValid by remember{ derivedStateOf { name.isNotBlank() } }

    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            modifier = modifier.height(IntrinsicSize.Min),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    modifier = modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = context.getString(R.string.add_folder_title),
                )
                FolderNameInput(
                    modifier = modifier.padding(top = 16.dp),
                    onValueChange = { name = it }
                )
                FolderColorSelect(
                    modifier = modifier.padding(top = 16.dp),
                    colorHexes = FolderColor.entries.map { it.colorHex }.drop(1),
                    onColorSelect = { colorHex = it }
                )
                Row(
                    modifier = modifier.padding(top = 16.dp)
                ) {
                    AddDialogCloseButton(
                        modifier = modifier,
                        buttonLabel = context.getString(R.string.add_folder_negative),
                        onClick = onDismissRequest
                    )
                    Spacer(modifier = modifier.size(12.dp))
                    AddDialogConfirmButton(
                        modifier = modifier,
                        buttonLabel = context.getString(R.string.add_folder_positive),
                        isNameValid = isNameValid,
                        onClick = {
                            addFolder(name, colorHex)
                            onDismissRequest()
                        },
                    )
                }
            }
        }
    }
}

@Composable
fun FolderNameInput(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    val context = LocalContext.current
    var input by remember { mutableStateOf("") }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = modifier.padding(end = 8.dp),
            text = context.getString(R.string.add_folder_name)
        )
        TextField(
            modifier = modifier.height(48.dp),
            value = input,
            singleLine = true,
            onValueChange = {
                onValueChange(it)
                input = it
            },
            placeholder = {
                FolderNameInputPlaceHolder(
                    placeHolderText = context.getString(R.string.add_folder_name_hint)
                )
            }
        )
    }
}

@Composable
fun FolderNameInputPlaceHolder(
    modifier: Modifier = Modifier,
    placeHolderText: String = ""
) {
    Text(
        modifier = modifier,
        text = placeHolderText
    )
}

@Composable
fun FolderColorSelect(
    modifier: Modifier = Modifier,
    colorHexes: List<String>,
    onColorSelect: (String) -> Unit
) {
    val context = LocalContext.current
    var selectedColorHex by remember { mutableStateOf(colorHexes.first()) }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = modifier.padding(end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                modifier = modifier.scale(1.2f),
                painter = painterResource(id = R.drawable.icon_folder),
                colorFilter = ColorFilter.tint(Color(parseColor(selectedColorHex))),
                contentDescription = ""
            )
            Text(
                modifier = modifier,
                text = context.getString(R.string.add_folder_color)
            )
        }
        FolderColorList(
            modifier = modifier.fillMaxWidth(),
            colorHexes = colorHexes,
            onColorSelect = {
                onColorSelect(it)
                selectedColorHex = it
            }
        )
    }
}

@Composable
fun FolderColorList(
    modifier: Modifier = Modifier,
    colorHexes: List<String>,
    onColorSelect: (String) -> Unit
) {
    val colors = colorHexes.map { Color(parseColor(it)) }

    var selectedColor by remember { mutableStateOf(colors.first()) }
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        colors.forEachIndexed { index, color ->
            FolderColorItem(
                modifier = Modifier
                    .clickable { selectedColor = color },
                color = color,
                colorSelected = (selectedColor == color),
                onClick = {
                    onColorSelect(colorHexes[index])
                    selectedColor = color
                }
            )
            Spacer(modifier = Modifier.size(4.dp))
        }
    }
}

@Composable
fun FolderColorItem(
    modifier: Modifier = Modifier,
    color: Color,
    colorSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(
            modifier = Modifier
                .size(28.dp)
                .drawBehind {
                    drawCircle(color = color)
                }
                .clickable {
                    onClick()
                }

        )
        if (colorSelected) {
            Image(
                modifier = modifier.padding(4.dp),
                painter = painterResource(id = R.drawable.icon_dot),
                colorFilter = ColorFilter.tint(Color.DarkGray),
                contentDescription = ""
            )
        }
    }
}

@Composable
fun AddDialogCloseButton(
    modifier: Modifier = Modifier,
    buttonLabel: String,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(text = buttonLabel)
    }
}

@Composable
fun AddDialogConfirmButton(
    modifier: Modifier = Modifier,
    buttonLabel: String,
    isNameValid: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = isNameValid
    ) {
        Text(text = buttonLabel)
    }
}
