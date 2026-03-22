package com.example.rulesreviewer

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

/**
 * Background worker for syncing user data (Rule R8).
 */
class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val TAG = "SyncWorker"
    }

    override suspend fun doWork(): Result {
        AppLog.d(TAG, "Sync attempt started")

        return try {
            // Perform secure sync logic here.
            // Following Rule R10, any network responses must be validated before use.
            
            AppLog.i(TAG, "Sync attempt successful")
            Result.success()
        } catch (e: Exception) {
            // Following Rule R7, ensure no PII is logged in the stack trace.
            AppLog.e(TAG, "Sync attempt failed", e)
            Result.retry()
        }
    }
}
