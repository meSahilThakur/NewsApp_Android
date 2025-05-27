package com.example.newsapp.presentation.ui.saved_articles

import android.R.attr.fontWeight
import android.R.attr.maxLines
import android.health.connect.datatypes.units.Length
import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.newsapp.domain.model.Article
import com.example.newsapp.presentation.navigation.Routes
import com.example.newsapp.presentation.viewmodel.SavedArticlesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedArticlesScreenUI(
    navController: NavController,
    viewModel: SavedArticlesViewModel = hiltViewModel()
) {
    val savedArticlesState by viewModel.savedArticlesState.collectAsState()
    val context = LocalContext.current                  //current context for Toast messages

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Saved Articles") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                savedArticlesState.isLoading -> CircularProgressIndicator()
                savedArticlesState.error.isNotEmpty() -> {
                    Text(
                        text = savedArticlesState.error,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                savedArticlesState.data.isNotEmpty() -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(
                            items = savedArticlesState.data,
                            key = {article -> article.url ?: article.hashCode()}
                        ) { article ->
                            ArticleCard(
                                article = article,
                                onArticleClick = {
                                    article.url?.let {
                                        navController.navigate(Routes.NewsDetailScreen(articleUrl = article.url))
                                    } ?: run {
                                        Toast.makeText(context, "Cannot open article: URL not available.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                    }
                }
                else -> {
                    Box(modifier = Modifier
                        .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "No saved articles yet.\nSave some articles from the News Feed!",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ArticleCard(
    article: Article,
    onArticleClick: (String)-> Unit
){
    val context = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable{
                article.url?.let {
                    onArticleClick(article.url)
                } ?: run {
                    Toast.makeText(context, "Cannot open article: URL not available.", Toast.LENGTH_SHORT).show()
                }
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = article.urlToImage,
                contentDescription = "article_image",
                modifier = Modifier
                    .size(100.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = article.title?: "No Title",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2, // Limit title to 2 lines
                    overflow = TextOverflow.Ellipsis // Add "..." if title overflows
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!article.sourceName.isNullOrEmpty()){
                        Text(
                            text = article.sourceName,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    if (!article.author.isNullOrEmpty()){
                        if (!article.sourceName.isNullOrEmpty()){
                            Text(
                                text = " - ",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        Text(
                            text = article.author,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}