package com.example.appbookzn

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith
import java.io.IOException
import org.junit.Assert.assertEquals

@RunWith(AndroidJUnit4::class)
class BookDaoTest {

    private lateinit var bookDao: BookDao
    private lateinit var bookDatabase: BookDatabase

    private val book1 = Book(title = "Perahu Kertas", author = "Dee Lestari", id = 1)
    private val book2 = Book(title = "Phone Tips", author = "Fauzan", id = 2)

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        bookDatabase = Room.inMemoryDatabaseBuilder(context, BookDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        bookDao = bookDatabase.bookDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        bookDatabase.close()
    }

    private fun insertOneBook() = runBlocking {
        bookDao.insert(book1)
    }

    private fun insertTwoBooks() = runBlocking {
        bookDao.insert(book1)
        bookDao.insert(book2)
    }

    @Test
    fun daoInsert_insertsBookIntoDb() = runBlocking {
        insertOneBook()
        val result = bookDao.getAll()
        assertEquals(1, result.size)
        assertEquals(book1.title, result[0].title)
        assertEquals(book1.author, result[0].author)
    }

    @Test
    fun daoGetAllBooks_returnsAllBooksFromDb() = runBlocking {
        insertTwoBooks()
        val result = bookDao.getAll()
        assertEquals(2, result.size)
        assertEquals(book1.title, result[0].title)
        assertEquals(book2.title, result[1].title)
    }

    @Test
    fun daoDeleteBook_removesBookFromDb() = runBlocking {
        insertTwoBooks()
        bookDao.delete(book1)
        val result = bookDao.getAll()
        assertEquals(1, result.size)
        assertEquals(book2.title, result[0].title)
    }

    @Test
    fun daoDeleteAllBooks_clearsTable() = runBlocking {
        insertTwoBooks()
        bookDao.deleteAll()
        val result = bookDao.getAll()
        assertEquals(0, result.size)
    }
}
