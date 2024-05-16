package com.sparta.imagesearch.presentation.util.dialog

import androidx.annotation.StringRes
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.sparta.imagesearch.presentation.util.compositionlocal.LocalButtonColors

sealed class DialogButton {
    abstract val labelId: Int
    abstract val onClick: () -> Unit

    data class PositiveDialogButton(
        @StringRes override val labelId: Int,
        override val onClick: () -> Unit,
        val enabled: Boolean = true
    ) : DialogButton()

    data class NegativeDialogButton(
        @StringRes override val labelId: Int,
        override val onClick: () -> Unit
    ) : DialogButton()
}

@Composable
fun DialogButtonWrapper(dialogButton: DialogButton) {
    when (dialogButton) {
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
fun NegativeDialogButton(@StringRes labelId: Int, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors =
        with(LocalButtonColors.current.primaryButton) {
            ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = disabledContainerColor,
                disabledContentColor = disabledContentColor
            )
        }
    ) {
        Text(
            text = stringResource(id = labelId),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


@Composable
fun PositiveDialogButton(
    @StringRes labelId: Int,
    onClick: () -> Unit,
    enabled: Boolean
) {
    Button(
        onClick = onClick,
        colors =
        with(LocalButtonColors.current.tertiaryButton) {
            ButtonDefaults.buttonColors(
                containerColor = containerColor,
                contentColor = contentColor,
                disabledContainerColor = disabledContainerColor,
                disabledContentColor = disabledContentColor
            )
        },
        enabled = enabled
    ) {
        Text(
            text = stringResource(id = labelId),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}