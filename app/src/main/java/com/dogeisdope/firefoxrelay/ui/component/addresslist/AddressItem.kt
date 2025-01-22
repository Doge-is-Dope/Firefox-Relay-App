package com.dogeisdope.firefoxrelay.ui.component.addresslist

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dogeisdope.firefoxrelay.R
import com.dogeisdope.firefoxrelay.data.RelayAddress
import java.time.ZonedDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddressItem(
    modifier: Modifier,
    item: RelayAddress,
    onAddressUpdate: (Long, String) -> Unit,
    onAddressDelete: (Long) -> Unit
) {
    val context = LocalContext.current
    val haptic = LocalHapticFeedback.current
    var openAlertDialog: DialogType? by remember { mutableStateOf(null) }

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
                    openAlertDialog = DialogType.Delete(
                        onDismissRequest = { openAlertDialog = null },
                        onConfirmation = {
                            openAlertDialog = null
                            onAddressDelete(item.id)
                        },
                        description = item.description.takeUnless { it.isBlank() },
                        emailAddress = item.address,
                    )
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
            MoreMenu(onCopyClick = {
                context.copyToClipboard(item.fullAddress)
            }, onEditClick = {
                openAlertDialog = DialogType.Update(
                    onDismissRequest = { openAlertDialog = null },
                    onConfirmation = {
                        openAlertDialog = null
                        onAddressUpdate(item.id, "test")
                    },
                    description = item.description
                )
            }, onDeleteClick = {
                openAlertDialog = DialogType.Delete(
                    onDismissRequest = { openAlertDialog = null },
                    onConfirmation = {
                        openAlertDialog = null
                        onAddressDelete(item.id)
                    },
                    description = item.description.takeUnless { it.isBlank() },
                    emailAddress = item.address,
                )
            })
        }

        openAlertDialog?.let {
            AddressActionDialog(it)
        }
    }
}

@Composable
fun MoreMenu(onCopyClick: () -> Unit, onEditClick: () -> Unit, onDeleteClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Rounded.MoreVert, contentDescription = "More")
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Copy") },
                onClick = {
                    onCopyClick()
                    expanded = false
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_copy),
                        contentDescription = "edit"
                    )
                })
            DropdownMenuItem(
                text = { Text("Edit") },
                onClick = {
                    onEditClick()
                    expanded = false
                },
                leadingIcon = {
                    Icon(
                        Icons.Rounded.Edit,
                        contentDescription = "edit"
                    )
                })
            DropdownMenuItem(
                text = { Text("Delete") },
                onClick = {
                    onDeleteClick()
                    expanded = false
                },
                leadingIcon = {
                    Icon(
                        Icons.Rounded.Delete,
                        contentDescription = "delete"
                    )
                })
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
        onAddressUpdate = { _, _ -> },
        onAddressDelete = {}
    )
}