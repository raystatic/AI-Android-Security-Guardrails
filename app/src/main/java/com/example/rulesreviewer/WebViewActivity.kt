package com.example.rulesreviewer

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

/**
 * Activity to load URLs from external sources securely (Rule R5, R6, R10).
 */
class WebViewActivity : ComponentActivity() {

    companion object {
        const val EXTRA_URL = "extra_url"
        private const val TAG = "WebViewActivity"
        
        // Rule R10: Define an allowlist of trusted domains.
        private val ALLOWED_DOMAINS = listOf("myapp.com", "www.myapp.com")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val url = intent.getStringExtra(EXTRA_URL)
        
        if (isValidUrl(url)) {
            setContent {
                AndroidView(
                    factory = { context ->
                        WebView(context).apply {
                            webViewClient = WebViewClient()
                            
                            // Rule R5: JavaScript is disabled by default for dynamic content.
                            settings.javaScriptEnabled = false
                            
                            // Rule R5: Explicitly disable file and content access.
                            settings.allowFileAccess = false
                            settings.allowContentAccess = false
                            
                            loadUrl(url!!)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }
        } else {
            AppLog.e(TAG, "Blocked attempt to load invalid or untrusted URL: $url")
            finish() // Close the activity if the URL is unsafe
        }
    }

    /**
     * Validates that the URL uses HTTPS and belongs to a trusted domain (Rule R4, R5, R10).
     */
    private fun isValidUrl(url: String?): Boolean {
        if (url == null) return false
        
        // Rule R4: Must be HTTPS
        if (!url.startsWith("https://")) return false
        
        return try {
            val uri = android.net.Uri.parse(url)
            val host = uri.host?.lowercase()
            
            // Rule R10: Validate against allowlist
            ALLOWED_DOMAINS.contains(host)
        } catch (e: Exception) {
            false
        }
    }
}
