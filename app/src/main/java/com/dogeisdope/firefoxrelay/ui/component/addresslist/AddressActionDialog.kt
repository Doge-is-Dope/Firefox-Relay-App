package com.dogeisdope.firefoxrelay.ui.component.addresslist

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AddressActionDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    description: String? = null,
    address: String,
) {
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = {
            Text(text = "Delete address?")
        },
        text = {
            Text(text = buildAnnotatedString {
                append("Deleting ")
                withStyle(
                    SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    ),
                ) { append(description ?: address) }
                append(" will remove it from your address list permanently.")
            })
        },
        confirmButton = {
            FilledTonalButton(onClick = { onConfirmation() }) {
                Text("Delete")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismissRequest() }) {
                Text("Cancel")
            }
        },
    )
}

@Preview
@Composable
fun AddressActionDialogPreview() {
    AddressActionDialog(
        onDismissRequest = {},
        onConfirmation = {},
        address = "0x1234567890",
    )
}