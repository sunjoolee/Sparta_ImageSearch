package com.sparta.imagesearch.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sparta.imagesearch.R

@Composable
fun AlertDialog(
    modifier: Modifier = Modifier,
    @DrawableRes iconId: Int? = null,
    @StringRes titleId: Int,
    @StringRes bodyId: Int,
    @StringRes dismissButtonLabelId: Int,
    onAlertDismiss: () -> Unit,
    @StringRes confirmButtonLabelId: Int,
    onAlertConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = onAlertDismiss
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
                iconId?.let {iconId ->
                    Image(
                        modifier = modifier
                            .scale(1.2f)
                            .padding(bottom = 16.dp),
                        painter = painterResource(id = iconId),
                        contentDescription = ""
                    )
                }
                Text(
                    text = stringResource(titleId),
                )
                Text(
                    text = stringResource(bodyId),
                )
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = onAlertConfirm
                    ) {
                        Text(text = stringResource(confirmButtonLabelId))
                    }
                    Spacer(modifier = modifier.size(12.dp))
                    Button(
                        onClick = onAlertDismiss
                    ) {
                        Text(text = stringResource(dismissButtonLabelId))
                    }
                }
            }
        }
    }
}