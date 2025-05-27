package com.example.newsapp.presentation.ui.news_detail

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.newsapp.presentation.viewmodel.NewsDetailViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailScreenUI(
    navController: NavController,
    viewModel: NewsDetailViewModel = hiltViewModel()
) {
    val articleState by viewModel.articleState.collectAsState()
    val isSavedState by viewModel.isSavedState.collectAsState()
    val saveActionState by viewModel.saveArticleState.collectAsState()
    val deleteActionState by viewModel.deleteArticleState.collectAsState()

    // State for Snackbar messages
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope() // Coroutine scope for launching Snackbar coroutines

    val context = LocalContext.current

    // --- Handle Snackbar Feedback for Save/Delete Actions ---
    // Use LaunchedEffect to show Snackbar when save/delete action state changes
    LaunchedEffect(saveActionState) {
        Log.d("NewsDetailUI", "Save Action State changed: $saveActionState")
        if (saveActionState.isSuccess) {
            snackbarHostState.showSnackbar(
                message = "Article saved successfully!",
                duration = SnackbarDuration.Short
            )
            viewModel.resetSaveArticleState() // Reset state after showing message
        } else if (saveActionState.isError) {
            snackbarHostState.showSnackbar(
                message = saveActionState.error ?: "Failed to save article.",
                duration = SnackbarDuration.Short
            )
            viewModel.resetSaveArticleState() // Reset state after showing message
        }
    }                         // Coroutine scope for launching Snackbar coroutines

    LaunchedEffect(deleteActionState) {
        if (deleteActionState.isSuccess) {
            snackbarHostState.showSnackbar(
                message = "Article deleted successfully!",
                duration = SnackbarDuration.Short
            )
            viewModel.resetDeleteArticleState() // Reset state after showing message
        } else if (deleteActionState.isError) {
            snackbarHostState.showSnackbar(
                message = deleteActionState.error ?: "Failed to delete article.",
                duration = SnackbarDuration.Short
            )
            viewModel.resetDeleteArticleState() // Reset state after showing message
        }
    }


    Scaffold(
        // Show Snackbar messages at the bottom
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = articleState.data?.title ?: "Article Details",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis                    // Add "..." if it overflows
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back_button"
                        )
                    }
                },
                actions = {
                    // Save/Delete Toggle Icon
                    // Show loading state for the icon while saving/deleting
                    if (saveActionState.isLoading || deleteActionState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp), // Spinner size
                            color = MaterialTheme.colorScheme.onPrimary // Spinner color
                        )
                    } else {
                        // Show save or delete icon based on saved status
                        IconButton(
                            onClick = {
                                // Call ViewModel function to toggle save status
                                // Pass the article data if available
                                articleState.data?.let {
                                    viewModel.toggleSaveStatus(it)
                                }
                            },
                            // Make the button disabled if article data is not yet loaded
                            enabled = articleState.data != null && !articleState.isLoading
                        ) {
                            // Choose icon based on the observed isSavedState
                            val icon = if (isSavedState.data) {
                                Icons.Filled.Favorite // Filled heart if saved
                            } else {
                                Icons.Outlined.FavoriteBorder // Outlined heart if not saved
                            }
                            Icon(
                                imageVector = icon,
                                contentDescription = if (isSavedState.data) "Delete from saved" else "Save article"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Use theme colors
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when {
                articleState.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                articleState.error.isNotEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = articleState.error
                        )
                    }
                }

                articleState.data != null -> {
                    val article = articleState.data
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {

                        // ARTICLE IMAGE IF AVAILABLE
                        if (!article?.urlToImage.isNullOrEmpty()) {
                            AsyncImage(
                                model = article.urlToImage,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // ARTICLE TITLE
                        Text(
                            text = article?.title ?: "No Title Available",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // ARTICLE SOURCE AND AUTHOR
                        Row(
                            modifier = Modifier,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (!article?.sourceName.isNullOrEmpty()) {
                                Text(
                                    text = article.sourceName,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color.Gray
                                )
                                if (!article?.author.isNullOrEmpty()) {
                                    Text(
                                        text = " - ",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = Color.Gray
                                    )
                                }
                            }
                            if (!article?.author.isNullOrEmpty()) {
                                Text(
                                    text = article.author,
                                    style = MaterialTheme.typography.titleSmall,
                                    color = Color.Gray
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        // PUBLISHED DATE
                        article?.publishedAt?.let { dateString ->
                            val formattedDate = try {
                                val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
                                val formatter = SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault())
                                val date = parser.parse(dateString)
                                date?.let { formatter.format(it) } ?: dateString
                            } catch (e: Exception) {
                                dateString // fallback
                            }

                            Text(
                                text = "Published: $formattedDate",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // ARTICLE DESCRIPTION
                        if (!article?.description.isNullOrEmpty()) {
                            Text(
                                text = article.description,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        // ARTICLE CONTENT
                        if (!article?.content.isNullOrEmpty()) {
                            Text(
                                text = article.content,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        // FOR READ FULL ARTICLE LINK
                        article?.url?.let { url ->
                            Text(
                                text = "Read Full Article",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = Bold,
                                modifier = Modifier
                                    .clickable {
                                        Log.d("NewsDetailUI", "Attempting to open URL: $url") // Keep this log

                                        scope.launch { // Use the coroutine scope
                                            Log.d("NewsDetailUI", "Attempting to open URL: $url") // Keep this log

                                            try {
                                                // Parse the URL into a Uri object
                                                val uri = Uri.parse(url)
                                                Log.d("NewsDetailUI", "Parsed URI: $uri")

                                                // *** USE THIS ALTERNATIVE INTENT CREATION METHOD ***
                                                val intent = Intent.makeMainSelectorActivity(Intent.ACTION_MAIN, Intent.CATEGORY_LAUNCHER)
                                                intent.data = uri // Set the URI data on this intent
                                                intent.action = Intent.ACTION_VIEW // Set the action

                                                // No need to explicitly check resolveActivity with makeMainSelectorActivity
                                                // The system should find a launcher activity that can view the data

                                                // Get the package manager from the current context (still good practice)
                                                val packageManager = context.packageManager

                                                // We can still check if there's ANY activity that can handle this final intent
                                                // though makeMainSelectorActivity is meant to guide the system better
                                                val resolvedActivity = intent.resolveActivity(packageManager)
                                                Log.d("NewsDetailUI", "Resolved Activity (makeMainSelectorActivity): $resolvedActivity")


                                                if (resolvedActivity != null) {
                                                    // If an activity is found, start it
                                                    context.startActivity(intent)
                                                    Log.d("NewsDetailUI", "Successfully started activity for URL (makeMainSelectorActivity): $url")
                                                } else {
                                                    // If no activity is found even with makeMainSelectorActivity
                                                    Log.e("NewsDetailUI", "No app found to open URL (makeMainSelectorActivity): $url")
                                                    snackbarHostState.showSnackbar(
                                                        message = "No app found to open link.",
                                                        duration = SnackbarDuration.Short
                                                    )
                                                }
                                            } catch (e: Exception) {
                                                // Handle exceptions during Uri parsing or Intent creation/launching
                                                Log.e("NewsDetailUI", "Error opening URL (makeMainSelectorActivity): $url", e)
                                                snackbarHostState.showSnackbar(
                                                    message = "Failed to open link: ${e.localizedMessage ?: "Unknown error"}",
                                                    duration = SnackbarDuration.Short
                                                )
                                            }
                                        }
                                    }
                            )
                        }

                    }
                }
            }
        }
    }
}