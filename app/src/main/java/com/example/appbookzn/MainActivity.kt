package com.example.appbookzn

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.*
import com.example.appbookzn.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Inisialisasi dua buku default
    private val book1 = Book(title = "Perahu Kertas", author = "Dee Lestari")
    private val book2 = Book(title = "Phone Tips", author = "Fauzan")

    private lateinit var binding: ActivityMainBinding
    private val bookViewModel: BookViewModel by viewModels()
    private lateinit var bookAdapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi Adapter
        bookAdapter = BookAdapter(this,
            onItemClick = { book ->
                Toast.makeText(this, "Clicked on ${book.title}", Toast.LENGTH_SHORT).show()
            },
            onDelete = { book ->
                bookViewModel.deleteBook(book) // ✅ Hapus langsung dari DB
            }
        )

        // Setup RecyclerView
        with(binding.rvBooks) {
            layoutManager = GridLayoutManager(this@MainActivity, 2)
            adapter = bookAdapter
        }

        // Inisialisasi database
        bookViewModel.bookDatabase = BookDatabase.getInstance(this)

        // Observasi perubahan data
        bookViewModel.books.observe(this) {
            bookAdapter.submitList(it)
        }

        // Tombol untuk menambah buku
        binding.btnAddBook.setOnClickListener {
            showAddBookDialog()
        }

        // Swipe untuk hapus
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder) = false
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                bookAdapter.deleteItem(viewHolder.adapterPosition)
            }
        }).attachToRecyclerView(binding.rvBooks)

        // ✅ Tambahkan buku jika belum ada di database
        bookViewModel.getBooks()
        bookViewModel.books.observe(this) { books ->
            if (books.none { it.title == book1.title && it.author == book1.author }) {
                bookViewModel.insertBook(book1)
            }
            if (books.none { it.title == book2.title && it.author == book2.author }) {
                bookViewModel.insertBook(book2)
            }
        }
    }

    // Fungsi untuk menampilkan dialog tambah buku
    private fun showAddBookDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_book, null)
        val titleInput = dialogView.findViewById<EditText>(R.id.etTitle)
        val authorInput = dialogView.findViewById<EditText>(R.id.etAuthor)

        AlertDialog.Builder(this)
            .setTitle("Tambah Buku")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                if (titleInput.text.isNotEmpty() && authorInput.text.isNotEmpty()) {
                    bookViewModel.insertBook(Book(titleInput.text.toString(), authorInput.text.toString()))
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }
}
