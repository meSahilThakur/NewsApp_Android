package com.example.newsapp.presentation.ui.news_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.newsapp.domain.model.Article
import com.example.newsapp.presentation.navigation.Routes
import com.example.newsapp.presentation.viewmodel.NewsListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsListScreenUI(
    navController: NavController,
    viewModel: NewsListViewModel = hiltViewModel()
) {
    val state by viewModel.newsState.collectAsState()

    val searchQuery by viewModel.searchQuery

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
    ) {

//        SearchBar(
//            text = searchQuery,
//            onSearch = { viewModel::onSearchQueryChange }
//        )

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = viewModel::onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(24.dp),
            leadingIcon = {
                Icon(Icons.Filled.Search, contentDescription = "Search")
            },
            placeholder = { Text("Search articles...") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    CircularProgressIndicator()
                }
            }

            state.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text(text = state.error!!)
                }
            }
            //optional
            state.getNewsData.isEmpty() && !state.isLoading && state.error == null -> {
                Text(text = "No news found.")
            }

            state.getNewsData.isNotEmpty() -> {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(state.getNewsData) { article ->
                        ArticleListItem(
                            article = article,
//                                onArticleClick = {navController.navigate(Routes.NewsDetailScreen)},
                            navController = navController
                        )
                    }
                }
            }
        }
    }

}

//@Composable
//fun SearchBar( text: String, onSearch: (String)-> Unit){
//    Box(modifier = Modifier.fillMaxWidth()){
//        OutlinedTextField(
//            value = text,
//            onValueChange = onSearch,
//            label = {Text("Search")},
//            modifier = Modifier.fillMaxWidth(),
//            shape = RoundedCornerShape(24.dp),
//            leadingIcon = {
//                Icon(Icons.Filled.Search, contentDescription = "Search")
//            },
//            placeholder = { Text("Search articles...") }
//        )
//    }
//}

@Composable
fun ArticleListItem(
    article: Article,
    navController: NavController,
//    onArticleClick: (Article) -> Unit,      // Lambda function for click event
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
//                onArticleClick(article)
                article.url?.let { url ->
                    navController.navigate(Routes.NewsDetailScreen(articleUrl = url))
                }
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = modifier
                .padding(16.dp)
        ) {
            if (!article.urlToImage.isNullOrEmpty()) {
                AsyncImage(
                    model = article.urlToImage,
                    contentDescription = "Article Image",
                    modifier = modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(
                text = article.title ?: "No Title",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (!article.sourceName.isNullOrEmpty()) {
                    Text(
                        text = article.sourceName,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    if (!article.author.isNullOrEmpty()) {
                        Text(" - ", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }
                }
                if (!article.author.isNullOrEmpty()) {
                    Text(
                        text = "By ${article.author}",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            if (!article.description.isNullOrEmpty()) {
                Text(
                    text = article.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}