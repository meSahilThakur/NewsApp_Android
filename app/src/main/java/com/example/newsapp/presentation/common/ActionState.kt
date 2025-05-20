package com.example.newsapp.presentation.common

// Data class for save article action feedback
data class SaveArticleState(
    val isLoading: Boolean = false, // True while the save action is in progress
    val isSuccess: Boolean = false, // True when the save action completed successfully
    val isError: Boolean = false, // True when the save action failed
    val error: String? = null // Error message if isError is true
)

// Data class for delete article action feedback
data class DeleteArticleState(
    val isLoading: Boolean = false, // True while the delete action is in progress
    val isSuccess: Boolean = false, // True when the delete action completed successfully
    val isError: Boolean = false, // True when the delete action failed
    val error: String? = null // Error message if isError is true
)