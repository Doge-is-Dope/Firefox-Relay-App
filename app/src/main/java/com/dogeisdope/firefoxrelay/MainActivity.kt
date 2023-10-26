package com.dogeisdope.firefoxrelay

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.dogeisdope.firefoxrelay.ui.component.addresslist.AddressList
import com.dogeisdope.firefoxrelay.ui.component.addresslist.AddressListViewModel
import com.dogeisdope.firefoxrelay.ui.component.addresslist.ErrorUiState
import com.dogeisdope.firefoxrelay.ui.theme.FirefoxRelayTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val addressListViewModel by viewModels<AddressListViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.light(
                Color.TRANSPARENT, Color.TRANSPARENT
            )
        )
        super.onCreate(savedInstanceState)
        setContent {
            FirefoxRelayTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) { MainScreen(addressListViewModel) }
            }
        }
    }
}

@Composable
fun MainScreen(addressListViewModel: AddressListViewModel) {
    val context = LocalContext.current
    var isAddAddress by remember { mutableStateOf(false) }
    var isShowSettings by remember { mutableStateOf(false) }
    var addressCount by remember { mutableStateOf(0) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            BottomAppBar(
                actions = {
                    IconButton(onClick = { addressListViewModel.fetchRelayAddresses() }) {
                        Icon(Icons.Rounded.Refresh, contentDescription = "Refresh")
                    }
                    IconButton(onClick = { isShowSettings = true }) {
                        Icon(Icons.Rounded.Settings, contentDescription = "Settings")
                    }
                },
                floatingActionButton = if (addressCount == 5) null else ({
                    FloatingActionButton(
                        onClick = {
                            isAddAddress = true
                            addressListViewModel.createNewAddress()
                        },
                        containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                    ) {
                        Icon(Icons.Rounded.Add, "Add")
                    }
                })
            )
        },
        content = { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                AddressList(
                    uiState = addressListViewModel.addressUiState.value,

                    onFetchAddress = {
                        if (isAddAddress) {
                            scope.launch { snackbarHostState.showSnackbar(context.getString(R.string.address_created)) }
                            isAddAddress = false
                        }
                        addressCount = it
                    },
                    onDeleteAddress = {
                        addressListViewModel.deleteRelayAddress(it)
                    },
                    onRefreshAddresses = {
                        addressListViewModel.fetchRelayAddresses()
                    })

                addressListViewModel.errorUiState.value.let {
                    if (it is ErrorUiState.Error) {
                        scope.launch {
                            snackbarHostState.showSnackbar(it.error.message ?: "Unknown error")
                        }
                    }
                }
            }
        }
    )

    if (isShowSettings) {
        SettingsDialog(onDismiss = { isShowSettings = false })
    }
}

@Composable
private fun SettingsDialog(onDismiss: () -> Unit) {
    Dialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "This is a dialog with buttons and an image.",
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    FirefoxRelayTheme {
        MainScreen(AddressListViewModel())
    }
}

@Preview
@Composable
fun SettingsDialogPreview() {
    SettingsDialog(onDismiss = {})
}