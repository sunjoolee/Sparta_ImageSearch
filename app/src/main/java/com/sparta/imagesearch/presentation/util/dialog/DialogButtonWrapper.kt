package com.sparta.imagesearch.presentation.util.dialog

import androidx.annotation.StringRes
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import com.sparta.imagesearch.R
import com.sparta.imagesearch.presentation.theme.ImageSearchColorScheme
import com.sparta.imagesearch.presentation.theme.ImageSearchTheme
import com.sparta.imagesearch.presentation.theme.disabled

sealed class DialogButton{
    abstract val labelId: Int
    abstract val onClick: () -> Unit
    data class PositiveDialogButton(
        @StringRes override val labelId: Int,
        override val onClick: () -> Unit,
        val enabled:Boolean = true
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
            PositiveDialogButton(
                labelId = dialogButton.labelId,
                onClick = dialogButton.onClick,
                enabled = dialogButton.enabled
                )
        is DialogButton.NegativeDialogButton ->
            NegativeDialogButton(dialogButton.labelId, dialogButton.onClick)
    }
}

@Composable
fun PositiveDialogButton(
    @StringRes labelId: Int,
    onClick: () -> Unit,
    enabled:Boolean) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = ImageSearchColorScheme.defaultScheme.tertiary,
            contentColor = ImageSearchColorScheme.defaultScheme.onTertiary,
            disabledContainerColor = ImageSearchColorScheme.defaultScheme.disabled,
            disabledContentColor = ImageSearchColorScheme.defaultScheme.onDisabled
        ),
        enabled = enabled
    ){
        Text(text = stringResource(id = labelId))
    }
}

@Composable
fun NegativeDialogButton(@StringRes labelId: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = ImageSearchColorScheme.defaultScheme.primary,
            contentColor = ImageSearchColorScheme.defaultScheme.onPrimary,
        )
    ){
        Text(text = stringResource(id = labelId))
    }
}