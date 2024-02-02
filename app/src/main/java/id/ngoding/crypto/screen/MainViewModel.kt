package id.ngoding.crypto.screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ngoding.crypto.domain.EncryptedPreferences
import id.ngoding.crypto.domain.KeyProviderService
import id.ngoding.crypto.model.Keys
import id.ngoding.crypto.utils.KeyPairHandler
import id.ngoding.crypto.utils.RequestState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferences: EncryptedPreferences,
    private val keyProviderService: KeyProviderService
) : ViewModel() {

    private var _apiKeysReady: MutableState<RequestState<Boolean>> =
        mutableStateOf(RequestState.Idle)
    val apiKeysReady: State<RequestState<Boolean>> = _apiKeysReady

    private var _apiKeys: MutableState<Keys?> = mutableStateOf(null)
    val apiKeys: State<Keys?> = _apiKeys

    init {
        fetchData()
    }

    private suspend fun fetchApiKeysAndStore(): RequestState<Boolean> {
        return try {
            KeyPairHandler.generateKeyPair()
            val publicKey = KeyPairHandler.getPublicKey()
            val fetchedData = fetchEncryptedApiKeys(publicKey = publicKey)
            if (fetchedData != null) {
                val decryptedData = KeyPairHandler.decryptData(encryptedData = fetchedData)
                val keys = Json.decodeFromString<Keys>(decryptedData)
                val result = preferences.saveEncryptedData(keys = keys)
                _apiKeys.value = preferences.readEncryptedData()
                RequestState.Success(data = result)
            } else {
                throw ApiKeysException(message = "Failed to Fetch API Keys.")
            }
        } catch (e: Exception) {
            RequestState.Error(message = "${e.message}")
        }
    }

    private suspend fun fetchEncryptedApiKeys(publicKey: String): String? {
        val response = keyProviderService.getEncryptedApiKeys(publicKey = publicKey)
        return if (response.isSuccessful) response.body()
        else throw ApiKeysException(message = response.message())
    }

    fun fetchData() {
        viewModelScope.launch(Dispatchers.Main) {
            _apiKeysReady.value = RequestState.Loading
            delay(1000)
            _apiKeysReady.value = fetchApiKeysAndStore()
        }
    }
}

class ApiKeysException(message: String) : Exception(message)