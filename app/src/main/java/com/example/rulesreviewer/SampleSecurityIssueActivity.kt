package com.example.rulesreviewer

import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import java.net.URL
import android.content.Context
import android.database.sqlite.SQLiteDatabase

/**
 * A sample activity that intentionally violates several security rules for testing the security review skill.
 */
class SampleSecurityIssueActivity : ComponentActivity() {

    // VIOLATION R1: Hardcoded API Key
    private val API_KEY = "AIzaSyB-EXAMPLE-KEY-12345"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val email = intent.getStringExtra("user_email")
        
        // VIOLATION R7: Logging PII (email) without gating on BuildConfig.DEBUG
        Log.d("SampleActivity", "User email: $email")

        // VIOLATION R2: Storing sensitive data in plain SharedPreferences
        val prefs = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        prefs.edit().putString("auth_token", "secret-token-123").apply()

        // VIOLATION R10: SQL Injection vulnerability
        val userId = intent.getStringExtra("user_id")
        val db = openOrCreateDatabase("test_db", Context.MODE_PRIVATE, null)
        val cursor = db.rawQuery("SELECT * FROM users WHERE id = " + userId, null)

        setContent {
            AndroidView(
                factory = { context ->
                    WebView(context).apply {
                        webViewClient = WebViewClient()
                        
                        // VIOLATION R5: JavaScript enabled for dynamic content without justification
                        settings.javaScriptEnabled = true
                        
                        // VIOLATION R5: File access enabled
                        settings.allowFileAccess = true
                        
                        // VIOLATION R4: Loading HTTP URL
                        loadUrl("http://insecure-endpoint.com/login?api_key=$API_KEY")
                    }
                },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
