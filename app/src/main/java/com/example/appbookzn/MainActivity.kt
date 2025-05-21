package com.example.appbookzn

import android.os.Bundle
<<<<<<< HEAD
=======
import android.util.Log
import android.widget.Button
>>>>>>> 75e8f7e59f7229caca7f9bd4e9408c8d3f417a8b
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
<<<<<<< HEAD
import androidx.recyclerview.widget.*
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

        bookAdapter = BookAdapter(this,
            onItemClick = { book ->
                Toast.makeText(this, "Clicked on ${book.title}", Toast.LENGTH_SHORT).show()
            },
            onDelete = { book ->
                bookViewModel.deleteBook(book)
            }
        )

        with(binding.rvBooks) {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = bookAdapter
        }


        bookViewModel.bookDatabase = BookDatabase.getInstance(this)


        bookViewModel.books.observe(this) {
            bookAdapter.submitList(it)
        }


        binding.btnAddBook.setOnClickListener {
            showAddBookDialog()
        }


        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                bookAdapter.deleteItem(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(binding.rvBooks)

        bookViewModel.getBooks()
        bookViewModel.books.observe(this) { books ->
            if (books.none { it.title == book1.title && it.author == book1.author }) {
                bookViewModel.insertBook(book1)
            }
            if (books.none { it.title == book2.title && it.author == book2.author }) {
                bookViewModel.insertBook(book2)
            }
        }
=======
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
>>>>>>> 75e8f7e59f7229caca7f9bd4e9408c8d3f417a8b
    }

    private fun showAddBookDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_book, null)
        val titleInput = dialogView.findViewById<EditText>(R.id.etTitle)
        val authorInput = dialogView.findViewById<EditText>(R.id.etAuthor)

        AlertDialog.Builder(this)
            .setTitle("Tambah Buku")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
<<<<<<< HEAD
                if (titleInput.text.isNotEmpty() && authorInput.text.isNotEmpty()) {
                    bookViewModel.insertBook(Book(titleInput.text.toString(), authorInput.text.toString()))
=======
                val title = titleInput.text.toString()
                val author = authorInput.text.toString()
                if (title.isNotEmpty() && author.isNotEmpty()) {
                    val newBook = Book(title = title, author = author)
                    bookViewModel.insertBook(newBook)
                    Toast.makeText(this, "Buku ditambahkan", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Judul dan Penulis harus diisi", Toast.LENGTH_SHORT).show()
>>>>>>> 75e8f7e59f7229caca7f9bd4e9408c8d3f417a8b
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }
<<<<<<< HEAD
}
//
=======

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
>>>>>>> 75e8f7e59f7229caca7f9bd4e9408c8d3f417a8b
