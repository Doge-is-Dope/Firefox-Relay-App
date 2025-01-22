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
fun AddressActionDialog(dialogType: DialogType) {
    AlertDialog(
        onDismissRequest = { dialogType.onDismissRequest() },
        title = { Text(text = dialogType.dialogTitle) },
        text = {
            when (dialogType) {
                is DialogType.Delete -> {
                    Text(text = buildAnnotatedString {
                        append("Deleting ")
                        withStyle(
                            SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold
                            ),
                        ) { append(dialogType.description ?: dialogType.emailAddress) }
                        append(" will remove it from your address list permanently.")
                    })
                }

                is DialogType.Update -> {
                    Text(text = dialogType.description)
                }
            }
        },
        confirmButton = {
            FilledTonalButton(onClick = { dialogType.onConfirmation() }) {
                Text(dialogType.dialogConfirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = { dialogType.onDismissRequest() }) {
                Text("Cancel")
            }
        },
    )
}

sealed class DialogType {
    abstract val dialogTitle: String
    abstract val dialogConfirmButtonText: String
    abstract val onDismissRequest: () -> Unit
    abstract val onConfirmation: () -> Unit

    data class Delete(
        override val onDismissRequest: () -> Unit,
        override val onConfirmation: () -> Unit,
        val description: String? = null,
        val emailAddress: String,
    ) : DialogType() {
        override val dialogTitle: String
            get() = "Delete address?"
        override val dialogConfirmButtonText: String
            get() = "Delete"
    }

    data class Update(
        override val onDismissRequest: () -> Unit,
        override val onConfirmation: () -> Unit,
        val description: String
    ) : DialogType() {
        override val dialogTitle: String
            get() = "Edit description"
        override val dialogConfirmButtonText: String
            get() = "Save"
    }
}

@Preview
@Composable
fun AddressActionDialogPreview() {
    AddressActionDialog(
        DialogType.Delete(
            onDismissRequest = {},
            onConfirmation = {},
            emailAddress = "0x1234567890",
        )
    )
}