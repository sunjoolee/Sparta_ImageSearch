package com.sparta.imagesearch.presentation.util.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sparta.imagesearch.R
import com.sparta.imagesearch.presentation.theme.Padding

@Composable
fun BaseDialog(
    modifier: Modifier = Modifier,
    dialogTitle: DialogTitle,
    dialogContent: DialogContent,
    dialogNegButton: DialogButton? = null,
    dialogPosButton: DialogButton,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = colorResource(id = R.color.theme_bg),
                contentColor = colorResource(id = R.color.white)
            )
        ) {
            Column(
                modifier = modifier.padding(Padding.default),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                DialogTitleWrapper(dialogTitle)
                Spacer(modifier = Modifier.padding(top = Padding.default))
                DialogContentWrapper(dialogContent)
                Spacer(modifier = Modifier.padding(top = Padding.default))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    dialogNegButton?.let {
                        DialogButtonWrapper(dialogButton = dialogNegButton)
                    }
                    Spacer(modifier = Modifier.padding(start = Padding.default))
                    DialogButtonWrapper(dialogButton = dialogPosButton)
                }
            }
        }
    }
}

@Preview(widthDp = 360)
@Composable
fun AlertDialogPreview(){
    BaseDialog(
        dialogTitle = DialogTitle.AlertDialogTitle(
            iconId = R.drawable.icon_warning,
            titleId = R.string.warning_delete_title
        ),
        dialogContent = DialogContent.AlertDialogContent(
            textId = R.string.warning_delete_body
        ),
        dialogNegButton = DialogButton.NegativeDialogButton(
            labelId = R.string.delete_folder_negative,
            onClick = {}
        ),
        dialogPosButton = DialogButton.PositiveDialogButton(
            labelId = R.string.delete_folder_negative,
            onClick = {}
        ),
        onDismissRequest = {}
    )
}

