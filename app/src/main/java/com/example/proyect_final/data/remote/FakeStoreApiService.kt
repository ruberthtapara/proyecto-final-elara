package com.example.proyect_final.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface FakeStoreApiService {
    @GET("https://firestore.googleapis.com/v1/projects/stylegen-ai-2d349/databases/(default)/documents/productos?pageSize=100")
    suspend fun getProducts(): FirestoreResponse

    companion object {
        const val BASE_URL = "https://fakestoreapi.com/"
    }
}
