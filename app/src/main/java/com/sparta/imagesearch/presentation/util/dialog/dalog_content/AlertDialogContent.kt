package com.sparta.imagesearch.presentation.util.dialog.dalog_content

import androidx.annotation.StringRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

@Composable
fun AlertDialogContent(@StringRes textId: Int) {
    Text(text = stringResource(id = textId))
}