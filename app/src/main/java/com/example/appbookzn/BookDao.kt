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
<<<<<<< HEAD
//
=======
>>>>>>> 75e8f7e59f7229caca7f9bd4e9408c8d3f417a8b
