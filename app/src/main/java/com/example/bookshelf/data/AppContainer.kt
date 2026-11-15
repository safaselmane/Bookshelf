package com.example.bookshelf.data
import com.example.bookshelf.network.BookApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val bookRepository: BookRepository
}

class DefaultAppContainer : AppContainer {

    private val baseUrl = "https://www.googleapis.com/books/v1/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: BookApiService by lazy {
        retrofit.create(BookApiService::class.java)
    }

    override val bookRepository: BookRepository by lazy {
        NetworkBookRepository(service)
    }
}
