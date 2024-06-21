package com.litcove.litcove.ui.main.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.litcove.litcove.data.model.Book
import kotlinx.coroutines.launch

class BookDetailsViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val db = FirebaseFirestore.getInstance()

    fun addBookToHistory(book: Book, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            auth.currentUser?.let {
                val bookDetails = mapOf(
                    "bookId" to book.bookId,
                    "title" to book.title,
                    "authors" to book.authors,
                    "description" to book.description,
                    "thumbnail" to book.thumbnail,
                    "addedAt" to System.currentTimeMillis()
                )

                db.collection("users")
                    .document(it.uid)
                    .collection("history")
                    .document(book.bookId)
                    .set(bookDetails)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e -> onFailure(e) }
            }
        }
    }

    fun addBookToCollection(book: Book, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            auth.currentUser?.let {
                val bookDetails = mapOf(
                    "bookId" to book.bookId,
                    "title" to book.title,
                    "authors" to book.authors,
                    "description" to book.description,
                    "thumbnail" to book.thumbnail,
                    "addedAt" to System.currentTimeMillis()
                )

                db.collection("users")
                    .document(it.uid)
                    .collection("collections")
                    .document(book.bookId)
                    .set(bookDetails)
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e -> onFailure(e) }
            }
        }
    }

    fun removeBookFromCollection(bookId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            auth.currentUser?.let {
                db.collection("users")
                    .document(it.uid)
                    .collection("collections")
                    .document(bookId)
                    .delete()
                    .addOnSuccessListener { onSuccess() }
                    .addOnFailureListener { e -> onFailure(e) }
            }
        }
    }

    fun isBookInCollection(bookId: String, onExist: () -> Unit, onNotExist: () -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            auth.currentUser?.let {
                db.collection("users")
                    .document(it.uid)
                    .collection("collections")
                    .document(bookId)
                    .get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            onExist()
                        } else {
                            onNotExist()
                        }
                    }
                    .addOnFailureListener { e -> onFailure(e) }
            }
        }
    }
}