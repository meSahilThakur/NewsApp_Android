package com.example.newsapp.util

sealed class Resource<out T> {
    object Loading: Resource<Nothing>()                             // Use 'object' as Loading is a singleton state without data or extra properties
    data class Success<out T>(val data: T): Resource<T>()           // Data class to hold the success payload
    data class Error(val error: String): Resource<Nothing>()        // Data class to hold the error message
}