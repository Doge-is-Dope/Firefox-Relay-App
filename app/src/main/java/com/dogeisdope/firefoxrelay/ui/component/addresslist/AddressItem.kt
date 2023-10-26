package com.dogeisdope.firefoxrelay.ui.component.addresslist

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dogeisdope.firefoxrelay.data.RelayAddress
import java.time.ZonedDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddressItem(modifier: Modifier, item: RelayAddress, onAddressDelete: (Long) -> Unit) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    var openAlertDialog by remember { mutableStateOf(false) }

    fun Context.copyToClipboard(text: CharSequence) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("address", text)
        clipboard.setPrimaryClip(clip)
    }

    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .combinedClickable(
                onClick = { context.copyToClipboard(item.fullAddress) },
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    openAlertDialog = true
                }
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = item.fullAddress,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Button(onClick = { context.copyToClipboard(item.fullAddress) }) {
                Text(text = "Copy")
            }
        }

        if (openAlertDialog) {
            AddressActionDialog(
                onDismissRequest = { openAlertDialog = false },
                onConfirmation = {
                    openAlertDialog = false
                    onAddressDelete(item.id)
                },
                description = item.description.takeUnless { it.isBlank() },
                address = item.address,
            )
        }
    }
}

@Preview
@Composable
fun AddressItemPreview() {
    AddressItem(
        modifier = Modifier,
        item = RelayAddress(
            id = 111,
            description = "Test",
            address = "hello",
            fullAddress = "hello@mozmail.com",
            enabled = true,
            generatedFor = "test",
            createdAt = ZonedDateTime.now(),
            lastModifiedAt = ZonedDateTime.now(),
        ),
        onAddressDelete = {}
    )
}