package com.example.appbookzn

sealed class BookItem {
    data class Header(val title: String) : BookItem()
    data class BookData(val book: Book) : BookItem()
}