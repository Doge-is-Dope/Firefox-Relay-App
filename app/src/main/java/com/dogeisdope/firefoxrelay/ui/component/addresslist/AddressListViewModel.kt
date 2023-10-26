package com.dogeisdope.firefoxrelay.ui.component.addresslist

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dogeisdope.firefoxrelay.repository.RelayRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddressListViewModel : ViewModel() {
    private val repository = RelayRepository()

    private val _addressUiState = mutableStateOf<AddressUiState>(AddressUiState.Loading)
    val addressUiState: State<AddressUiState> get() = _addressUiState
    private val _errorUiState = mutableStateOf<ErrorUiState>(ErrorUiState.None)
    val errorUiState: State<ErrorUiState> get() = _errorUiState

    fun fetchRelayAddresses() {
        viewModelScope.launch {
            try {
                val result = repository.getRelayAddresses().transformResponse()
                _addressUiState.value = AddressUiState.Addresses(result.orEmpty())
            } catch (e: Exception) {
                _errorUiState.value = ErrorUiState.Error(e)
            }

        }
    }

    fun deleteRelayAddress(id: Long) {
        viewModelScope.launch {
            try {
                repository.deleteRelayAddress(id).transformResponse()
                fetchRelayAddresses()
            } catch (e: Exception) {
                _errorUiState.value = ErrorUiState.Error(e)
            }
        }
    }

    fun createNewAddress() {
        viewModelScope.launch {
            try {
                repository.createRelayAddress(getCurrentDateTime()).transformResponse()
                fetchRelayAddresses()
            } catch (e: Exception) {
                _errorUiState.value = ErrorUiState.Error(e)
            }
        }
    }

    private fun getCurrentDateTime(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("MM/dd hh:mm a")
        return currentDateTime.format(formatter)
    }

    private fun <T> Response<T>.transformResponse(): T? {
        if (isSuccessful) {
            return body()
        } else {
            throw Exception(message())
        }
    }
}