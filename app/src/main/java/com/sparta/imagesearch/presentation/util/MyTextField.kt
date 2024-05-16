package com.sparta.imagesearch.presentation.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import com.sparta.imagesearch.R
import com.sparta.imagesearch.presentation.theme.Padding
import com.sparta.imagesearch.presentation.theme.scheme

val SEARCH_BAR_LEADING_ICON_SCALE = 1.5f
val SEARCH_BAR_CLEAR_ICON_SCALE = 0.6f

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
            valueText = value,
            clearValueText = { onValueChange("") },
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
            valueText = value.text,
            clearValueText = { onValueChange(value.copy(text = "")) },
            innerTextField = innerTextField,
            leadingIconId = leadingIconId,
            placeholderTextId = placeholderTextId,
            clearTextFieldButton = clearTextFieldButton
        )
    }
}

@Composable
fun MyDecorationBox(
    valueText: String,
    clearValueText: () -> Unit,
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
                        .weight(0.1f)
                        .scale(SEARCH_BAR_LEADING_ICON_SCALE),
                    painter = painterResource(id = leadingIconId),
                    colorFilter = ColorFilter.tint(MaterialTheme.scheme.tertiary),
                    contentDescription = ""
                )
                Spacer(modifier = Modifier.padding(end = Padding.default))
            }
            Box(modifier = Modifier.weight(0.8f)) {
                placeholderTextId?.let {
                    if (valueText.isBlank()) Text(
                        text = stringResource(id = placeholderTextId),
                        color = MaterialTheme.scheme.disabled,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                innerTextField()
            }
            if (clearTextFieldButton && valueText.isNotBlank()) {
                Image(
                    modifier = Modifier
                        .weight(0.1f)
                        .scale(SEARCH_BAR_CLEAR_ICON_SCALE)
                        .padding(Padding.small)
                        .clickable { clearValueText() },
                    painter = painterResource(id = R.drawable.icon_clear_textfield),
                    colorFilter = ColorFilter.tint(MaterialTheme.scheme.disabled),
                    contentDescription = ""
                )
            }
        }
    }
}
