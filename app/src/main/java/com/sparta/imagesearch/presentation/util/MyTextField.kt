package com.sparta.imagesearch.presentation.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.sparta.imagesearch.R
import com.sparta.imagesearch.presentation.theme.Padding
import com.sparta.imagesearch.presentation.theme.scheme


val SEARCH_BAR_LEADING_ICON_SIZE = 24.dp
val SEARCH_BAR_CLEAR_ICON_SIZE = 18.dp

@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    @DrawableRes leadingIconId: Int? = null,
    @StringRes placeholderTextId: Int? = null,
    clearTextFieldButton: Boolean = false
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        cursorBrush = SolidColor(MaterialTheme.scheme.tertiary),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.scheme.onSurface
        ),
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    ) { innerTextField ->
        MyDecorationBox(
            value = value,
            onValueChange = onValueChange,
            innerTextField = innerTextField,
            leadingIconId = leadingIconId,
            placeholderTextId = placeholderTextId,
            clearTextFieldButton = clearTextFieldButton
        )
    }
}

@Composable
fun MyTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    @DrawableRes leadingIconId: Int? = null,
    @StringRes placeholderTextId: Int? = null,
    clearTextFieldButton: Boolean = false
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        cursorBrush = SolidColor(MaterialTheme.scheme.tertiary),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.scheme.onSurface
        ),
        singleLine = true,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    ) { innerTextField ->
        MyDecorationBox(
            value= value,
            onValueChange = onValueChange,
            innerTextField = innerTextField,
            leadingIconId = leadingIconId,
            placeholderTextId = placeholderTextId,
            clearTextFieldButton = clearTextFieldButton
        )
    }
}


@Composable
fun MyDecorationBox(
    value: String,
    onValueChange: (String) -> Unit,
    innerTextField: @Composable () -> Unit,
    @DrawableRes leadingIconId: Int? = null,
    @StringRes placeholderTextId: Int? = null,
    clearTextFieldButton: Boolean = false
) {
    Surface(
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Row(
            modifier = Modifier.padding(Padding.default)
        ) {
            leadingIconId?.let {
                Image(
                    modifier = Modifier
                        .padding(end = Padding.default)
                        .size(SEARCH_BAR_LEADING_ICON_SIZE),
                    painter = painterResource(id = leadingIconId),
                    colorFilter = ColorFilter.tint(MaterialTheme.scheme.tertiary),
                    contentDescription = ""
                )
            }
            Box {
                placeholderTextId?.let {
                    if (value.isBlank()) Text(
                        text = stringResource(id = placeholderTextId),
                        color = MaterialTheme.scheme.disabled,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                innerTextField()
            }
            if (clearTextFieldButton && value.isNotBlank()) {
                Image(
                    modifier = Modifier
                        .size(SEARCH_BAR_CLEAR_ICON_SIZE)
                        .padding(end = Padding.default)
                        .clickable { onValueChange("") },
                    painter = painterResource(id = R.drawable.icon_clear_textfield),
                    colorFilter = ColorFilter.tint(MaterialTheme.scheme.disabled),
                    contentDescription = ""
                )
            }
        }
    }
}

@Composable
fun MyDecorationBox(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    innerTextField: @Composable () -> Unit,
    @DrawableRes leadingIconId: Int? = null,
    @StringRes placeholderTextId: Int? = null,
    clearTextFieldButton: Boolean = false
) {
    Surface(
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Row(
            modifier = Modifier.padding(Padding.default)
        ) {
            leadingIconId?.let {
                Image(
                    modifier = Modifier
                        .padding(end = Padding.default)
                        .size(SEARCH_BAR_LEADING_ICON_SIZE),
                    painter = painterResource(id = leadingIconId),
                    colorFilter = ColorFilter.tint(MaterialTheme.scheme.tertiary),
                    contentDescription = ""
                )
            }
            Box(
                modifier = Modifier
                    .weight(0.8f)
                    .padding(end = Padding.medium)
                    .align(alignment = Alignment.CenterVertically),
            ) {
                placeholderTextId?.let {
                    if (value.text.isBlank()) Text(
                        text = stringResource(id = placeholderTextId),
                        color = MaterialTheme.scheme.disabled,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                innerTextField()
            }
            if (clearTextFieldButton && value.text.isNotBlank()) {
                Image(
                    modifier = Modifier
                        .size(SEARCH_BAR_CLEAR_ICON_SIZE)
                        .padding(end = Padding.medium)
                        .clickable { onValueChange(value.copy(text = "")) },
                    painter = painterResource(id = R.drawable.icon_clear_textfield),
                    colorFilter = ColorFilter.tint(MaterialTheme.scheme.disabled),
                    contentDescription = ""
                )
            }
        }
    }
}