package com.example.appbookzn

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.appbookzn.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val book1 = Book(title = "Perahu Kertas", author = "Dee Lestari")
    private val book2 = Book(title = "Phone Tips", author = "Fauzan")

    private lateinit var binding: ActivityMainBinding
    private val bookViewModel: BookViewModel by viewModels()
    private lateinit var bookAdapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bookAdapter = BookAdapter(
            context = this,
            onItemClick = { book ->
                Toast.makeText(this, "Clicked on ${book.title}", Toast.LENGTH_SHORT).show()
            },
            onItemLongClick = { book ->
                showDeleteConfirmation(book)
            }
        )


        val layoutManager = GridLayoutManager(this, 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (bookAdapter.getItemViewType(position)) {
                    0 -> 2
                    1 -> 1
                    else -> 1
                }
            }
        }

        binding.rvBooks.layoutManager = layoutManager
        binding.rvBooks.adapter = bookAdapter


        bookViewModel.bookDatabase = BookDatabase.getInstance(this)


        bookViewModel.books.observe(this) { books ->
            val items = mutableListOf<BookItem>()
            items.add(BookItem.Header("📚 Book Notes"))
            items.addAll(books.map { BookItem.BookData(it) })
            bookAdapter.submitList(items)

            if (books.none { it.title == book1.title && it.author == book1.author }) {
                bookViewModel.insertBook(book1)
            }
            if (books.none { it.title == book2.title && it.author == book2.author }) {
                bookViewModel.insertBook(book2)
            }
        }

        binding.btnAddBook.setOnClickListener {
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
                    bookViewModel.insertBook(Book(title = title, author = author))
                } else {
                    Toast.makeText(this, "Judul dan Penulis harus diisi", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showDeleteConfirmation(book: Book) {
        AlertDialog.Builder(this)
            .setTitle("Hapus Buku")
            .setMessage("Apakah kamu yakin ingin menghapus '${book.title}'?")
            .setPositiveButton("Ya") { _, _ ->
                bookViewModel.deleteBook(book)
            }
            .setNegativeButton("Tidak", null)
            .show()
    }
}
