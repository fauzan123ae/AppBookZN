package com.example.appbookzn

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(
    private val context: Context,
    private val onItemClick: (Book) -> Unit,
    private val onItemLongClick: (Book) -> Unit
) : ListAdapter<BookItem, RecyclerView.ViewHolder>(BookItemDiffCallback()) {

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_BOOK = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is BookItem.Header -> VIEW_TYPE_HEADER
            is BookItem.BookData -> VIEW_TYPE_BOOK
        }
    }

    inner class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val headerTitle: TextView = view.findViewById(R.id.headerTitle)
        fun bind(header: BookItem.Header) {
            headerTitle.text = header.title
        }
    }

    inner class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val bookTitle: TextView = view.findViewById(R.id.bookTitle)
        private val bookAuthor: TextView = view.findViewById(R.id.bookAuthor)

        fun bind(book: Book) {
            bookTitle.text = book.title
            bookAuthor.text = "by ${book.author}"

            itemView.setOnClickListener {
                onItemClick(book)
            }

            itemView.setOnLongClickListener {
                onItemLongClick(book)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val view = inflater.inflate(R.layout.item_header, parent, false)
                HeaderViewHolder(view)
            }
            VIEW_TYPE_BOOK -> {
                val view = inflater.inflate(R.layout.item_book, parent, false)
                BookViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is BookItem.Header -> (holder as HeaderViewHolder).bind(item)
            is BookItem.BookData -> (holder as BookViewHolder).bind(item.book)
        }
    }

    class BookItemDiffCallback : DiffUtil.ItemCallback<BookItem>() {
        override fun areItemsTheSame(oldItem: BookItem, newItem: BookItem): Boolean {
            return when {
                oldItem is BookItem.Header && newItem is BookItem.Header -> oldItem.title == newItem.title
                oldItem is BookItem.BookData && newItem is BookItem.BookData -> oldItem.book.id == newItem.book.id
                else -> false
            }
        }

        override fun areContentsTheSame(oldItem: BookItem, newItem: BookItem): Boolean {
            return oldItem == newItem
        }
    }
}
