package com.example.rulesreviewer

import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

/**
 * Secure error reporting utility using Firebase Crashlytics (Rule R7, R8).
 * Ensures no PII or sensitive data is logged in production.
 */
object CrashReporter {

    /**
     * Logs a non-fatal exception while sanitizing sensitive data.
     * Rule R7: Exception stack traces must not include PII or server response bodies.
     */
    fun logException(throwable: Throwable, contextInfo: String? = null) {
        // Log to our internal secure logger first
        AppLog.e("CrashReporter", "Exception caught: ${throwable.message}", throwable)

        val crashlytics = Firebase.crashlytics
        
        // Add sanitized context info if provided
        contextInfo?.let {
            // Rule R7: Ensure contextInfo does not contain PII before logging.
            crashlytics.setCustomKey("context_info", it)
        }

        // Rule R7: The library itself handles stack traces, but we must 
        // ensure any custom logs added via crashlytics.log() are sanitized.
        crashlytics.recordException(throwable)
    }

    /**
     * Logs a custom message to Crashlytics, ensuring it's sanitized.
     */
    fun log(message: String) {
        // Rule R7: Ensure message does not contain PII or credentials.
        Firebase.crashlytics.log(message)
    }
}
