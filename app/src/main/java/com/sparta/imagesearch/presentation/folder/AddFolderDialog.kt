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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sparta.imagesearch.R
import com.sparta.imagesearch.domain.FolderColor
import com.sparta.imagesearch.util.DialogButtons

@Composable
fun AddFolderDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    addFolder: (String, String) -> Unit
) {
//    val (folderName, setFolderName) = remember { mutableStateOf("") }
    var folderName by remember { mutableStateOf("") }
    val setFolderName = {it:String -> folderName = it}

    val (folderColorHex, setFolderColorHex) = remember { mutableStateOf(FolderColor.COLOR1.colorHex) }

    val nameValid by remember { derivedStateOf { folderName.isNotBlank() } }

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
                    text = stringResource(R.string.add_folder_title),
                )
                FolderNameInput(
                    modifier = modifier.padding(top = 16.dp),
                    value = folderName,
                    onValueChange = setFolderName
                )
                FolderColorSelect(
                    modifier = modifier.padding(top = 16.dp),
                    colorHexes = FolderColor.entries.map { it.colorHex }.drop(1),
                    selectedColorHex = folderColorHex,
                    onColorSelect = setFolderColorHex
                )
                DialogButtons(
                    modifier = modifier.padding(top = 16.dp),
                    dismissButtonLabelId = R.string.add_folder_negative,
                    onDismissRequest = onDismissRequest,
                    confirmButtonLabelId = R.string.add_folder_positive,
                    enableConfirmButton = nameValid,
                    onConfirmRequest = { addFolder(folderName, folderColorHex) }
                )
            }
        }
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
            modifier = modifier.padding(end = 8.dp),
            text = stringResource(R.string.add_folder_name)
        )
        TextField(
            modifier = modifier.height(48.dp),
            value = value,
            singleLine = true,
            onValueChange = onValueChange,
            placeholder = {
                FolderNameInputPlaceHolder(
                    placeHolderText = stringResource(R.string.add_folder_name_hint)
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
    selectedColorHex: String,
    onColorSelect: (String) -> Unit
) {

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = modifier.padding(end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = modifier.scale(1.2f),
                painter = painterResource(id = R.drawable.icon_folder),
                colorFilter = ColorFilter.tint(Color(parseColor(selectedColorHex))),
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
        colorHexes.forEach {colorHex ->
            FolderColorItem(
                modifier = Modifier,
                color = Color(parseColor(colorHex)),
                colorSelected = (colorHex == selectedColorHex),
                onClick = {onColorSelect(colorHex)}
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
                modifier = modifier
                    .padding(4.dp)
                    .scale(1.2f),
                painter = painterResource(id = R.drawable.icon_dot),
                colorFilter = ColorFilter.tint(Color.DarkGray),
                contentDescription = ""
            )
        }
    }
}

