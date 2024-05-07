package com.sparta.imagesearch.util

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun DialogButtons(
    modifier: Modifier = Modifier,
    @StringRes dismissButtonLabelId: Int,
    onDismissRequest: () -> Unit,
    @StringRes confirmButtonLabelId: Int,
    enableConfirmButton: Boolean,
    onConfirmRequest: () -> Unit = {}
) {
    Row(
        modifier = modifier.padding(top = 16.dp)
    ) {
        DialogDismissButton(
            modifier = modifier,
            buttonLabelId = dismissButtonLabelId,
            onClick = onDismissRequest
        )

        Spacer(modifier = modifier.size(12.dp))

        DialogConfirmButton(
            modifier = modifier,
            buttonLabelId = confirmButtonLabelId,
            enabled = enableConfirmButton,
            onClick = {
                onConfirmRequest()
                onDismissRequest()
            }
        )
    }
}

@Composable
fun DialogDismissButton(
    modifier: Modifier = Modifier,
    @StringRes buttonLabelId: Int,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick
    ) {
        Text(text = stringResource(id = buttonLabelId))
    }
}

@Composable
fun DialogConfirmButton(
    modifier: Modifier = Modifier,
    @StringRes buttonLabelId: Int,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled
    ) {
        Text(text = stringResource(id = buttonLabelId))
    }
}
