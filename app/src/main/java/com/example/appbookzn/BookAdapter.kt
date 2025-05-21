package com.example.appbookzn

<<<<<<< HEAD
import android.content.Context
=======
>>>>>>> 75e8f7e59f7229caca7f9bd4e9408c8d3f417a8b
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
<<<<<<< HEAD
import androidx.recyclerview.widget.*
import com.example.appbookzn.R

class BookAdapter(
    private val context: Context,
    private val onItemClick: (Book) -> Unit,
=======
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(
>>>>>>> 75e8f7e59f7229caca7f9bd4e9408c8d3f417a8b
    private val onDelete: (Book) -> Unit
) : ListAdapter<Book, BookAdapter.BookViewHolder>(BookDiffCallback()) {

    inner class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bookTitle: TextView = view.findViewById(R.id.bookTitle)
        val bookAuthor: TextView = view.findViewById(R.id.bookAuthor)

        fun bind(book: Book) {
            bookTitle.text = book.title
            bookAuthor.text = "by ${book.author}"
<<<<<<< HEAD
            itemView.setOnClickListener {
                onItemClick(book)
=======

            itemView.setOnLongClickListener {
                onDelete(book)
                true
>>>>>>> 75e8f7e59f7229caca7f9bd4e9408c8d3f417a8b
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
<<<<<<< HEAD

    fun deleteItem(position: Int) {
        val book = getItem(position)
        onDelete(book)
        notifyItemRemoved(position)
    }
}

class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }
}
//
=======
}
>>>>>>> 75e8f7e59f7229caca7f9bd4e9408c8d3f417a8b
