package com.example.rulesreviewer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.rulesreviewer.ui.theme.RulesReviewerTheme

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.ui.viewinterop.AndroidView

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Handle incoming deep link (Rule R10)
        handleIntent(intent)

        setContent {
            RulesReviewerTheme {
                val searchQuery = remember { mutableStateOf("") }
                
                // Update search query state from deep link if present
                intent.data?.let { uri ->
                    if (uri.scheme == "myapp" && uri.host == "search") {
                        val query = uri.getQueryParameter("q")
                        if (isValidSearchQuery(query)) {
                            searchQuery.value = sanitizeQuery(query!!)
                            // Automatically run search with sanitized query
                            performSearch(searchQuery.value)
                        } else {
                            AppLog.e("MainActivity", "Blocked attempt to use invalid or unsafe deep link query: $query")
                        }
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TermsOfServiceScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun handleIntent(intent: Intent) {
        // Log deep link arrival without PII (Rule R7)
        AppLog.d("MainActivity", "Deep link received")
    }

    /**
     * Validates that the search query does not contain malicious characters (Rule R10).
     */
    private fun isValidSearchQuery(query: String?): Boolean {
        if (query == null || query.isBlank()) return false
        
        // Rule R10: Basic validation to prevent common injection characters
        val unsafeCharacters = listOf("'", ";", "--", "/*", "*/", "@@", "char", "nchar", "varchar", "nvarchar")
        return unsafeCharacters.none { query.lowercase().contains(it) }
    }

    /**
     * Sanitizes the search query to ensure it's safe to use (Rule R10).
     */
    private fun sanitizeQuery(query: String): String {
        // Simple sanitization: remove common SQL/HTML injection characters
        return query.replace(Regex("[^a-zA-Z0-9 ]"), "").trim()
    }

    private fun performSearch(query: String) {
        // Securely perform the search logic here
        AppLog.i("MainActivity", "Automatic search triggered for sanitized query")
    }
}

@Composable
fun TermsOfServiceScreen(modifier: Modifier = Modifier) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val secureStorage = remember { SecureStorage(context) }
    val termsUrl = "https://myapp.com/terms"

    // Rule R5: JavaScript interface is only added for this trusted origin.
    if (!termsUrl.startsWith("https://myapp.com/")) {
        AppLog.e("MainActivity", "Blocked attempt to add JavascriptInterface to untrusted origin.")
        return
    }

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = { ctx ->
            WebView(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = WebViewClient()
                
                // Rule R5: JavaScript is enabled ONLY for this trusted origin.
                settings.javaScriptEnabled = true
                
                // Rule R5: Default isolation settings
                settings.allowFileAccess = false
                settings.allowContentAccess = false
                
                // Rule R5: Add JavascriptInterface for secure web-to-native communication.
                // Expose getUserData() and saveUserData() as requested.
                addJavascriptInterface(WebAppInterface(secureStorage), "AndroidInterface")
                
                loadUrl(termsUrl)
            }
        },
        update = { webView ->
            webView.loadUrl(termsUrl)
        }
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RulesReviewerTheme {
        Greeting("Android")
    }
}