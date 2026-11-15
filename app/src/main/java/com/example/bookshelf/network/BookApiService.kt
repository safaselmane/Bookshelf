package com.example.bookshelf.network

import com.example.bookshelf.model.Book
import com.example.bookshelf.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BookApiService {
    @GET("volumes")
    suspend fun searchBooks(@Query("q") query: String): SearchResponse

    @GET("volumes/{id}")
    suspend fun getBook(@Path("id") id: String): Book
}
