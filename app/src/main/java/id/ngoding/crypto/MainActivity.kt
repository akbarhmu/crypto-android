package id.ngoding.crypto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import id.ngoding.crypto.screen.MainScreen
import id.ngoding.crypto.ui.theme.EncryptedPreferencesTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EncryptedPreferencesTheme {
                MainScreen(
                    apiKeysReady = ,
                    apiKeys = ,
                    onTryAgain = {}
                )
            }
        }
    }
}
