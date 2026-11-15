package com.example.bookshelf.data
import com.example.bookshelf.model.Book
import com.example.bookshelf.network.BookApiService

interface BookRepository {
    suspend fun getBooks(query: String): List<Book>
}

class NetworkBookRepository(
    private val api: BookApiService
) : BookRepository {

    override suspend fun getBooks(query: String): List<Book> {
        val ids = api.searchBooks(query).items.orEmpty().map { it.id }
        return ids.map { api.getBook(it) }
    }
}