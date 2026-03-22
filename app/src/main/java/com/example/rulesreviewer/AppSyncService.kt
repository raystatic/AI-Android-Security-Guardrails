package com.example.rulesreviewer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager

/**
 * Background service for application synchronization (Rule R3).
 * This service triggers a WorkManager task for reliable execution (Rule R8).
 * Registered in manifest with android:exported="false".
 */
class AppSyncService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        AppLog.d("AppSyncService", "Service started")
        
        // Trigger a one-time sync task via WorkManager (Rule R8)
        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>().build()
        WorkManager.getInstance(applicationContext).enqueue(syncRequest)
        
        // The service can stop itself after enqueuing the work
        stopSelf()
        
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        AppLog.d("AppSyncService", "Service destroyed")
    }
}

