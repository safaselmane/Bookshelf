package com.example.bookshelf

import com.example.bookshelf.data.NetworkBookRepository
import com.example.bookshelf.model.Book
import com.example.bookshelf.model.BookItem
import com.example.bookshelf.model.ImageLinks
import com.example.bookshelf.model.SearchResponse
import com.example.bookshelf.model.VolumeInfo
import com.example.bookshelf.network.BookApiService
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

// Faux service : aucune vraie requête réseau
class FakeBookApiService : BookApiService {
    override suspend fun searchBooks(query: String): SearchResponse =
        SearchResponse(listOf(BookItem("1"), BookItem("2")))

    override suspend fun getBook(id: String): Book =
        Book(id, VolumeInfo("Livre $id", ImageLinks("http://img/$id")))
}

class BookRepositoryTest {

    @Test
    fun repository_returnsBooksFromFakeService() = runTest {
        val repo = NetworkBookRepository(FakeBookApiService())

        val books = repo.getBooks("jazz")

        assertEquals(2, books.size)
        assertEquals("Livre 1", books[0].volumeInfo.title)
    }
}