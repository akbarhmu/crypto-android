package id.ngoding.crypto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import id.ngoding.crypto.screen.MainScreen
import id.ngoding.crypto.screen.MainViewModel
import id.ngoding.crypto.ui.theme.EncryptedPreferencesTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EncryptedPreferencesTheme {
                val viewModel: MainViewModel = hiltViewModel()
                val apiKeysReady by viewModel.apiKeysReady
                val apiKeys by viewModel.apiKeys

                MainScreen(
                    apiKeysReady = apiKeysReady,
                    apiKeys = apiKeys,
                    onTryAgain = { viewModel.fetchData() }
                )
            }
        }
    }
}
