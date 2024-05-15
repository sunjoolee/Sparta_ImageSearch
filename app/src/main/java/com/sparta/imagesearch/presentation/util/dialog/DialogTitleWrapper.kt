package com.sparta.imagesearch.presentation.util.dialog

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sparta.imagesearch.presentation.theme.Padding

val ALERT_TITLE_ICON_SIZE_DP = 32.dp
sealed class DialogTitle{
    abstract val titleId: Int
    data class DefaultDialogTitle(
        @StringRes override val titleId:Int
    ): DialogTitle()
    data class AlertDialogTitle(
        @DrawableRes val iconId:Int,
        @StringRes override val titleId:Int
    ): DialogTitle()
}

@Composable
fun DialogTitleWrapper(dialogTitle: DialogTitle){
    when(dialogTitle){
        is DialogTitle.DefaultDialogTitle -> DefaultDialogTitle(dialogTitle.titleId)
        is DialogTitle.AlertDialogTitle -> AlertDialogTitle(dialogTitle.iconId, dialogTitle.titleId)
    }
}

@Composable
fun DefaultDialogTitle(@StringRes titleId:Int) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = stringResource(titleId),
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center
    )
}

@Composable
fun AlertDialogTitle(@DrawableRes iconId:Int, @StringRes titleId:Int) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
                modifier = Modifier.size(ALERT_TITLE_ICON_SIZE_DP),
                painter = painterResource(id = iconId),
                contentDescription = ""
            )
        Spacer(modifier = Modifier.padding(top = Padding.default))
        Text(
            text = stringResource(titleId),
            style = MaterialTheme.typography.titleLarge,
        )
    }
}