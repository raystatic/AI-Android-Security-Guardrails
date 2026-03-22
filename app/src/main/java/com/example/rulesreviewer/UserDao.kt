package com.example.rulesreviewer

import androidx.room.Dao
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface UserDao {
    /**
     * Searches for a user by email using a parameterized query.
     * Following security rule R10, we use named bind parameters to prevent SQL injection.
     * @RawQuery is avoided here as it is a fixed query.
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?
}
