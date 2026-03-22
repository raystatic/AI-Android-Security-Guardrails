package com.example.rulesreviewer

import android.webkit.JavascriptInterface

/**
 * JavaScript interface for hybrid web-to-native communication (Rule R5).
 * Exposes secure data access to trusted web content.
 */
class WebAppInterface(private val secureStorage: SecureStorage) {

    /**
     * Exposes user data (e.g., Date of Birth) to the web layer.
     * Rule R7: Sensitive data is returned directly to the trusted web layer, never logged.
     */
    @JavascriptInterface
    fun getUserData(): String {
        // Return sensitive PII securely from EncryptedSharedPreferences (Rule R2).
        return secureStorage.getDateOfBirth() ?: ""
    }

    /**
     * Allows the web layer to save user data securely.
     * Rule R5: All inputs from JavaScript MUST be validated and sanitized.
     */
    @JavascriptInterface
    fun saveUserData(data: String?) {
        if (data == null || data.isBlank()) {
            AppLog.e("WebAppInterface", "Attempted to save empty or null user data.")
            return
        }

        // Rule R5/R10: Validate data format (e.g., simple date format check)
        val dateRegex = Regex("""\d{4}-\d{2}-\d{2}""") // Expects YYYY-MM-DD
        if (dateRegex.matches(data)) {
            secureStorage.saveDateOfBirth(data)
            AppLog.i("WebAppInterface", "User data saved successfully from web layer.")
        } else {
            AppLog.e("WebAppInterface", "Blocked attempt to save invalid data format.")
        }
    }
}
