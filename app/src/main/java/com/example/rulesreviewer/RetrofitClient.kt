package com.example.rulesreviewer

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Interface defining the User API service.
 */
interface UserService {
    @GET("users")
    suspend fun getUsers(): List<User>
}

/**
 * Singleton Retrofit client for secure network operations (Rule R4).
 * Enforces HTTPS for all connections.
 */
object RetrofitClient {
    private const val BASE_URL = "https://api.myapp.com/v1/"

    val instance: UserService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(UserService::class.java)
    }
}
