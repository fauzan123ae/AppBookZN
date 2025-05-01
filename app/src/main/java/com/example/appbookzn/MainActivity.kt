package com.example.appbookzn

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    val bookViewModel: BookViewModel by viewModels()
    lateinit var bookRecyclerView: RecyclerView
    lateinit var bookAdapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bookRecyclerView = findViewById(R.id.rvBooks)
        bookRecyclerView.layoutManager = LinearLayoutManager(this)

        bookViewModel.bookDatabase = BookDatabase.getInstance(this)

        val observer = Observer<List<Book>> { books ->
            if (!::bookAdapter.isInitialized) {
                bookAdapter = BookAdapter { book ->
                    showDeleteDialog(book)
                }
                bookRecyclerView.adapter = bookAdapter
            }

            bookAdapter.submitList(books)

            Log.d("BookList", "Jumlah buku di database: ${books.size}")
            for (book in books) {
                Log.d("BookList", "📖 ${book.title} - ${book.author}")
            }

            if (books.isNotEmpty()) {
                Toast.makeText(this, "${books.size} buku ditampilkan", Toast.LENGTH_SHORT).show()
            }
        }

        bookViewModel.books.observe(this, observer)

        val btnAdd: Button = findViewById(R.id.btnAddBook)
        btnAdd.setOnClickListener {
            showAddBookDialog()
        }

        bookViewModel.getBooks()
    }

    private fun showAddBookDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_book, null)
        val titleInput = dialogView.findViewById<EditText>(R.id.etTitle)
        val authorInput = dialogView.findViewById<EditText>(R.id.etAuthor)

        AlertDialog.Builder(this)
            .setTitle("Tambah Buku")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val title = titleInput.text.toString()
                val author = authorInput.text.toString()
                if (title.isNotEmpty() && author.isNotEmpty()) {
                    val newBook = Book(title = title, author = author)
                    bookViewModel.insertBook(newBook)
                    Toast.makeText(this, "Buku ditambahkan", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Judul dan Penulis harus diisi", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showDeleteDialog(book: Book) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Buku")
            .setMessage("Yakin ingin menghapus \"${book.title}\"?")
            .setPositiveButton("Hapus") { _, _ ->
                bookViewModel.deleteBook(book)
                Toast.makeText(this, "Buku dihapus", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
