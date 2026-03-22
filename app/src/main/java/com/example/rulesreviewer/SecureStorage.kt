package com.example.rulesreviewer

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * Utility for secure data storage using EncryptedSharedPreferences (Rule R1, R2).
 * Uses AES256-GCM encryption for key-value data.
 */
class SecureStorage(context: Context) {

    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    /**
     * Saves a secure JWT token.
     */
    fun saveToken(token: String) {
        sharedPreferences.edit().putString("JWT_TOKEN", token).apply()
    }

    /**
     * Retrieves the secure JWT token.
     */
    fun getToken(): String? {
        return sharedPreferences.getString("JWT_TOKEN", null)
    }

    /**
     * Saves the user's date of birth securely (Rule R1, R2).
     * PII is always encrypted at rest.
     */
    fun saveDateOfBirth(dob: String) {
        sharedPreferences.edit().putString("USER_DOB", dob).apply()
    }

    /**
     * Retrieves the user's date of birth securely.
     */
    fun getDateOfBirth(): String? {
        return sharedPreferences.getString("USER_DOB", null)
    }

    /**
     * Clears the secure JWT token (on logout).
     */
    fun clearToken() {
        sharedPreferences.edit().remove("JWT_TOKEN").apply()
    }
}
