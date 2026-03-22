package com.example.rulesreviewer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * BroadcastReceiver for application inter-app communication.
 * This receiver is registered in the manifest with android:exported="true".
 * Following Rule R3, it must be protected with a signature permission.
 */
class AppBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        AppLog.d("AppBroadcastReceiver", "Broadcast received")
        
        // Handle the incoming broadcast securely.
        // Validate all incoming intent extras (Rule R6, R10).
    }
}
