package id.ngoding.crypto.domain

import id.ngoding.crypto.model.Keys

interface EncryptedPreferences {
    fun saveEncryptedData(keys: Keys): Boolean
    fun readEncryptedData(): Keys?
    fun areApiKeysReady(): Boolean
}