package com.dogeisdope.firefoxrelay.ui.component.addresslist

import com.dogeisdope.firefoxrelay.data.RelayAddress

sealed class AddressUiState {
    data class Addresses(val addresses: List<RelayAddress>) : AddressUiState()
    data object Loading : AddressUiState()
}

sealed class ErrorUiState {
    data class Error(val error: Throwable) : ErrorUiState()
    data object None : ErrorUiState()
}