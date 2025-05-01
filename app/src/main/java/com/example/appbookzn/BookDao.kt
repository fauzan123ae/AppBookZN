package com.example.appbookzn

import androidx.room.*

@Dao
interface BookDao {
    @Query("SELECT * FROM Book")
    fun getAll(): List<Book>

    @Insert
    fun insert(vararg book: Book)

    @Update
    fun update(book: Book)

    @Delete
    fun delete(book: Book)

    @Query("DELETE FROM Book")
    fun deleteAll()
}
