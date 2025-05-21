package com.example.appbookzn

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookViewModel : ViewModel() {

    var bookDatabase: BookDatabase? = null

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> get() = _books

    fun getBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            val list = bookDatabase?.bookDao()?.getAll() ?: emptyList()
            _books.postValue(list)
        }
    }

    fun insertBook(book: Book) {
        viewModelScope.launch(Dispatchers.IO) {
            bookDatabase?.bookDao()?.insert(book)
            getBooks()
        }
    }

    fun clearBooks() {
        viewModelScope.launch(Dispatchers.IO) {
            bookDatabase?.bookDao()?.deleteAll()
            getBooks()
        }
    }
    fun deleteBook(book: Book) {
        viewModelScope.launch(Dispatchers.IO) {
            bookDatabase?.bookDao()?.delete(book)
            getBooks()
        }
    }

}
//