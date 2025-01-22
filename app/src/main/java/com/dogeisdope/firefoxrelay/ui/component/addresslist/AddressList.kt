package com.dogeisdope.firefoxrelay.ui.component.addresslist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dogeisdope.firefoxrelay.data.RelayAddress
import java.time.ZonedDateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AddressList(
    uiState: AddressUiState,
    onFetchAddress: (Int) -> Unit,
    onUpdateAddress: (Long, String) -> Unit,
    onDeleteAddress: (Long) -> Unit,
    onRefreshAddresses: () -> Unit,
) {

    LaunchedEffect(Unit) {
        onRefreshAddresses()
    }

    when (uiState) {
        is AddressUiState.Addresses -> {
            onFetchAddress(uiState.addresses.size)
            LazyColumn(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(all = 16.dp)
            ) {
                items(uiState.addresses, key = { it.id }) { address ->
                    AddressItem(
                        modifier = Modifier.animateItemPlacement(),
                        item = address,
                        onAddressUpdate = onUpdateAddress,
                        onAddressDelete = { onDeleteAddress(address.id) }
                    )
                }
            }
        }

        is AddressUiState.Loading -> Text(
            text = "Loading...",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )
    }
}

@Preview
@Composable
fun AddressListPreview() {
    AddressList(
        uiState = AddressUiState.Addresses(
            List(3) { index ->
                RelayAddress(
                    (index + 1).toLong(),
                    "hello",
                    "hello@mozmail.com",
                    "test ${(index + 1)}",
                    index % 2 == 0,
                    "test",
                    ZonedDateTime.now(),
                    ZonedDateTime.now()
                )
            }),
        onFetchAddress = {},
        onUpdateAddress = { _, _ -> },
        onDeleteAddress = {},
        onRefreshAddresses = {}
    )
}


