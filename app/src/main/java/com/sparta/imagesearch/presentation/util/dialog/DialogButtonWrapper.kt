package com.sparta.imagesearch.presentation.util.dialog

import androidx.annotation.StringRes
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.sparta.imagesearch.R

sealed class DialogButton{
    abstract val labelId: Int
    abstract val onClick: () -> Unit
    data class PositiveDialogButton(
        @StringRes override val labelId: Int,
        override val onClick: () -> Unit
    ): DialogButton()
    data class NegativeDialogButton(
        @StringRes override val labelId: Int,
        override val onClick: () -> Unit
    ): DialogButton()
}

@Composable
fun DialogButtonWrapper(dialogButton: DialogButton){
    when(dialogButton){
        is DialogButton.PositiveDialogButton ->
            PositiveDialogButton(dialogButton.labelId, dialogButton.onClick)
        is DialogButton.NegativeDialogButton ->
            NegativeDialogButton(dialogButton.labelId, dialogButton.onClick)
    }
}

@Composable
fun PositiveDialogButton(@StringRes labelId: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            //TODO change color resource to theme color
            containerColor = colorResource(id = R.color.theme_accent),
            contentColor = colorResource(id = R.color.theme_secondary),
        )
    ){
        Text(text = stringResource(id = labelId))
    }
}

@Composable
fun NegativeDialogButton(@StringRes labelId: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            //TODO change color resource to theme color
            containerColor = colorResource(id = R.color.theme_secondary),
            contentColor = colorResource(id = R.color.white),
        )
    ){
        Text(text = stringResource(id = labelId))
    }
}