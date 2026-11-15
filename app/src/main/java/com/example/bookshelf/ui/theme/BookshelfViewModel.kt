package com.example.bookshelf.ui.theme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.bookshelf.BookshelfApplication
import com.example.bookshelf.data.BookRepository
import kotlinx.coroutines.launch

sealed interface BookshelfUiState {
    //liste de paires (titre, url de l'image)
    data class Success(val thumbnails: List<Pair<String, String>>) : BookshelfUiState
    object Error : BookshelfUiState
    object Loading : BookshelfUiState
}

class BookshelfViewModel(private val repo: BookRepository) : ViewModel() {

    var uiState: BookshelfUiState by mutableStateOf(BookshelfUiState.Loading)
        private set

    init {
        loadBooks("jazz history")
    }

    fun loadBooks(query: String) {
        viewModelScope.launch {
            uiState = BookshelfUiState.Loading
            uiState = try {
                val books = repo.getBooks(query)
                val items = books.mapNotNull { book ->
                    val url = book.volumeInfo.imageLinks?.thumbnail
                        ?.replace("http://", "https://")
                    if (url != null) (book.volumeInfo.title ?: "") to url else null
                }
                BookshelfUiState.Success(items)
            } catch (e: Exception) {
                android.util.Log.e("BOOKSHELF", "Erreur : ${e.javaClass.simpleName} - ${e.message}", e)
                BookshelfUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val app = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                        as BookshelfApplication
                return BookshelfViewModel(app.container.bookRepository) as T
            }
        }
    }
}