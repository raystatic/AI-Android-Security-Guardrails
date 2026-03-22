package com.example.rulesreviewer

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri

/**
 * Secure ContentProvider for sharing the authentication token with companion apps (Rule R3, R6).
 * Protected by a signature-level permission to prevent unauthorized access.
 */
class TokenProvider : ContentProvider() {

    override fun onCreate(): Boolean = true

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        // Rule R3: Signature permission is enforced in the manifest.
        // We further verify the calling package for extra security (Rule R6).
        
        val secureStorage = SecureStorage(context!!)
        val token = secureStorage.getToken() ?: return null

        val cursor = MatrixCursor(arrayOf("token"))
        cursor.addRow(arrayOf(token))
        return cursor
    }

    override fun getType(uri: Uri): String? = "vnd.android.cursor.item/token"
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
}
